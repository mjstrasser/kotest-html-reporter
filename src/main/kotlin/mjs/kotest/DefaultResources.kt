package mjs.kotest

const val DefaultCss = """
* {
    font-family: sans-serif;
}

.code {
    font-family: monospace;
    background-color: #EEEEEE;
}

.timestamp {
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

.block-col {
    flex: 0 0 1em;
    margin: 2px;
}

.result-col {
    flex: 0 0 1em;
    margin: 2px;
}

.name-col {
    flex: 1;
    margin: 2px;
    border-top: 1px solid #EEEEEE;
    border-left: 1px solid #EEEEEE;
    box-shadow: 4px 4px 2px #EEEEEE;
}

.Failure {
    background: #fceef1;
}

.ChildFailure {
    background: #fceef1;
}

.Failure:hover {
    background: #ffd7e5;
    cursor: pointer;
}

.Success {
    background: #eefae6;
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

const val DefaultJavaScript = """
function toggleItem(id) {
    const item = document.getElementById(id);
    if (item) {
        const hidden = item.style.display === "none";
        item.style.display = hidden ? "block" : "none";
    }
}
"""