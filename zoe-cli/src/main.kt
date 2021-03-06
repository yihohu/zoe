// Copyright (c) 2020 Adevinta.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.adevinta.oss.zoe.cli

import com.adevinta.oss.zoe.cli.commands.*
import com.adevinta.oss.zoe.cli.utils.useResource
import com.github.ajalt.clikt.core.subcommands
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import kotlin.system.exitProcess

@FlowPreview
@ExperimentalCoroutinesApi
fun main(args: Array<String>) {
    val returnCode = useResource(resource = startKoin { }, onClose = KoinApplication::close) {

        val command = ZoeCommandLine().subcommands(
            topicsCommand(),
            schemasCommand(),
            groupsCommands(),
            configCommands(),
            lambdaCommands(),
            versionCommands()
        )

        command
            .runCatching { main(args) }
            .onFailure { command.printErr(it) }
            .fold(onSuccess = { 0 }, onFailure = { 1 })
    }

    exitProcess(returnCode)
}