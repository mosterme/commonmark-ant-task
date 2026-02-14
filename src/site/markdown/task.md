# Commonmark Ant Task

## Description

A simple Markdown Ant Task. Because sometimes using Maven site is just overkill.

## Parameters

| Attribute   | Description                                                                                | Required                |
|-------------|--------------------------------------------------------------------------------------------|-------------------------|
| source      | specifies a single markdown *file* or *directory* with markdown files to be converted.     | Yes                     |
| destination | specifies the output *directory* for the converted html files.                             | Yes                     |
| overwrite   | overwrite destination files, even if they are newer than their corresponding source files. | No; defaults to "false" |
| verbose     | log all the files that are being converted.                                                | No; defaults to "false" |

## Examples

Create the task definition for the markdown task.

        <taskdef name="markdown" classname="mosterme.commonmark.AntTask">
            <classpath>
                <pathelement location="lib/commonmark-ant-task-0.1.jar"/>
                <pathelement location="lib/commonmark-0.27.1.jar"/>
            </classpath>
        </taskdef>

Convert a single markdown file to html.

        <markdown source="readme.md" destination="target"/>

Convert all files from a source directory.

        <markdown source="src/site/markdown" destination="target/html"/>
