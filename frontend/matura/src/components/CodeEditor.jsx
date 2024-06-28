import {Editor} from "@monaco-editor/react";
import PropTypes from "prop-types";

export const CodeEditor = ({ language, startingCode, onChangeCallback }) => {

    return (
        <Editor
            defaultLanguage={language}
            defaultValue={startingCode}
            onChange={text => onChangeCallback(text)}
            theme='vs-dark'
            height="100%"
            width='100dvw'
            options={{
                wordWrap: 'on',
                minimap: {
                    enabled: false
                },
                inlayHints: {
                    enabled: 'on',
                },
                scrollbar: {
                    horizontalScrollbarSize: 0
                }
            }}
        />
    )
}
CodeEditor.propTypes = {
    language: PropTypes.string.isRequired,
    startingCode: PropTypes.string,
    onChangeCallback: PropTypes.func
}
