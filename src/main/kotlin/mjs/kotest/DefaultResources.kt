/*

   Copyright 2022 Michael Strasser.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package mjs.kotest

/**
 * Default CSS to load if `html-reporter.css` is not available on the classpath.
 * It should contain an exact copy of that file.
 */
internal const val DefaultCss = """
* {
    font-family: sans-serif;
}

h2 {
    margin-top: 20px;
    margin-bottom: 0;
}

.code {
    font-family: monospace;
    background-color: #EEEEEE;
}

.timestamp {
    line-height: 1.4;
    font-size: 1em;
    color: #666666;
}

.duration {
    color: gray;
}

.toc-entries {
    columns: 2;
}

.toc-line {
    display: flex;
    margin: 2px;
}

.toc-col {
    flex: 1;
    margin: 2px;
}

.line {
    display: flex;
    margin: 2px;
}

.pre-test {
    height: 10px;
}

.block-col {
    flex: 0 0 1em;
    margin: 2px;
}

.result-col {
    flex: 0 0 1em;
    margin: 2px;
}

.result-Ignored {
    color: grey;
}

.result-Success {
    color: green;
}

.result-Failure, .result-ChildFailure {
    color: red;
}

.name-col {
    flex: 1;
    margin: 2px;
    border-top: 1px solid #EEEEEE;
    border-left: 1px solid #EEEEEE;
    box-shadow: 4px 4px 2px #F6F6F6;
}

.error-message {
    font-family: monospace;
    margin-bottom: 2px;
}

.Failure {
    background: #fcf3f3;
}

.ChildFailure {
    background: #fcf3f3;
}

.Failure:hover {
    background: #fdd9e4;
    cursor: pointer;
}

.Success {
    background: #f2faeb;
}

.Ignored {
    background: ivory;
    color: gray;
}

.duration-col {
    flex: 0 0 6em;
    text-align: right;
    margin: 2px;
}

.shadow-box {
    margin: 12px 6px 6px 6px;
    padding: 6px;
    border-top: 1px solid #EEEEEE;
    border-left: 1px solid #EEEEEE;
    box-shadow: 6px 6px 3px #EEEEEE;
}

.tagline {
    text-align: center;
}
"""