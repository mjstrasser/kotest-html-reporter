package mjs.kotest

internal const val DefaultCss = """
* {
    font-family: sans-serif;
}

h2 {
	margin-top: 20px;
	margin-bottom: 0px;
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
    margin: 6px;
    padding: 6px;
    border-top: 1px solid #EEEEEE;
    border-left: 1px solid #EEEEEE;
    box-shadow: 6px 6px 3px #EEEEEE;
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

.description {
    margin: 6px;
    padding: 6px;
    border-top: 1px solid #EEEEEE;
    border-left: 1px solid #EEEEEE;
    box-shadow: 6px 6px 3px #EEEEEE;
}
"""

internal const val DefaultJavaScript = """
function toggleItem(id) {
    const item = document.getElementById(id);
    if (item) {
        const hidden = item.style.display === "none";
        item.style.display = hidden ? "block" : "none";
    }
}
"""