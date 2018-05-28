/**
* Copyright 2016 ZuInnoTe (Jörn Franke) <zuinnote@gmail.com>
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
**/

package org.zuinnote.spark.bitcoin.example

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.hadoop.conf._
import org.apache.spark.graphx._

import org.apache.hadoop.mapreduce._
import org.apache.hadoop.fs._
import org.apache.hadoop.io._

import org.zuinnote.hadoop.bitcoin.format.common._
import org.zuinnote.hadoop.bitcoin.format.mapreduce._

/**
* Author: Jörn Franke <zuinnote@gmail.com>
*
*/

/**

*
* Constructs a PageRank out of Bitcoin Transactions.
*
* Vertex: Bitcoin address (only in the old public key and the new P2P hash format)
* Edge: A directed edge from one source Bitcoin address to a destination Bitcoin address (A link between the input of one transaction to the originating output of another transaction via the transaction hash and input/output indexes)
*
* Problem to solve: Find the top 5 bitcoin addresses with the highest number of inputs from other bitcoin addresses
*/

object SparkScalaBitcoinTransactionsByDate {
   def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Spark-Scala-Graphx Bitcoin PageRank (hadoopcryptoledger)")
	val sc=new SparkContext(conf)
	val hadoopConf = new Configuration();
	hadoopConf.set("hadoopcryptoledger.bitcoinblockinputformat.filter.magic","F9BEB4D9");
	showTimes(sc,hadoopConf,args(0),args(1))
	//jobExportToGEXP(sc,hadoopConf,args(0),args(1),from,to)
	sc.stop()
      }

	def showTimes(sc: SparkContext, hadoopConf: Configuration, inputFile: String, outputFile: String): Unit = {
		val bitcoinBlocksRDD = sc.newAPIHadoopFile(inputFile, classOf[BitcoinBlockFileInputFormat], classOf[BytesWritable], classOf[BitcoinBlock],hadoopConf)
		val tempOutputFile = "./tempData";
		bitcoinBlocksRDD.map(c => c._2).map(c => (c.getTime() + ";" + c.getTransactions().size())).saveAsTextFile(tempOutputFile)
		val keys = sc.textFile(tempOutputFile)
		val mapk = keys.map(_.split(";"))
		val mapkv = mapk.map(c => (c(0).toInt, c(1).toInt)).repartition(1)
		mapkv.sortByKey().map(c => c._1 + ";" + c._2).saveAsTextFile(outputFile)
	}
}
