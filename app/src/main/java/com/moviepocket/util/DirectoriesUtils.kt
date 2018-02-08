package com.moviepocket.util

import android.content.Context
import android.os.Environment
import android.os.StatFs
import java.io.File
import java.util.ArrayList

/**
 * Created by diegosantos on 12/16/17.
 */
fun getDirectoryFiles(filePath: String): ArrayList<File> {
    val dir = File(filePath)

    val directoryFiles = ArrayList<File>()
    val files = dir.listFiles()

    if (files != null) {
        for (file in files) {
            if (file.isDirectory) {
                directoryFiles.addAll(getDirectoryFiles(file.absolutePath))
            } else {
                directoryFiles.add(file)
            }
        }
    }

    return directoryFiles
}

fun dirSizeGB(dir: File, blockSize: Long): Double? {

    if (dir.exists()) {
        val bytes = getDirectorySize(dir, blockSize)
        if (bytes < 1024) return 0.0
        val exp = (Math.log(bytes.toDouble()) / Math.log(1024.0)).toInt()
        val pre = "KMGTPE"[exp - 1] + ""

        return if (pre == "G") bytes / Math.pow(1024.0, exp.toDouble()) else 0.0
    }

    return 0.0
}

fun dirSizeMB(dir: File, blockSize: Long): Double? {

    if (dir.exists()) {
        val bytes = getDirectorySize(dir, blockSize)
        if (bytes < 1024) return 0.0
        val exp = (Math.log(bytes.toDouble()) / Math.log(1024.0)).toInt()
        val pre = "KMGTPE"[exp - 1] + ""

        return if (pre == "M") bytes / Math.pow(1024.0, exp.toDouble()) else 0.0
    }

    return 0.0
}

fun getDirectorySize(directory: File, blockSize: Long): Long {
    val files = directory.listFiles()
    if (files != null) {

        // space used by directory itself
        var size = directory.length()

        for (file in files) {
            if (file.isDirectory) {
                // space used by subdirectory
                size += getDirectorySize(file, blockSize)
            } else {
                // file size need to rounded up to full block sizes
                // (not a perfect function, it adds additional block to 0 sized files
                // and file who perfectly fill their blocks)
                size += (file.length() / blockSize + 1) * blockSize
            }
        }
        return size
    } else {
        return 0
    }
}

fun getAvailableMemoryInBytes(context: Context): Long {
    val filesDir = context.filesDir.absolutePath

    val stats = StatFs(filesDir)
    val availableBlocks = stats.availableBlocks
    val blockSizeInBytes = stats.blockSize

    return (availableBlocks * blockSizeInBytes).toLong()
}

fun externalMemoryAvailable(): Boolean {
    return android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED
}

fun getAvailableInternalMemorySize(): Long {
    val path = Environment.getDataDirectory()
    val stat = StatFs(path.path)
    val blockSize = stat.blockSize.toLong()
    val availableBlocks = stat.availableBlocks.toLong()
    return availableBlocks * blockSize
}
