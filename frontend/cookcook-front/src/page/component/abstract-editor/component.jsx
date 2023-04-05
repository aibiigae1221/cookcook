import { useState, useEffect, useMemo } from 'react';
import {Editor, EditorState,
        RichUtils, convertToRaw, CompositeDecorator, Modifier,
        getDefaultKeyBinding, KeyBindingUtil}
                                            from 'draft-js';


import 'draft-js/dist/Draft.css';

import "./AbstractEditor.css";


const styles = {
  root: {
    fontFamily: '\'Georgia\', serif',
    padding: 20,
    width: 600,
  },
  buttons: {
    marginBottom: 10,
  },
  urlInputContainer: {
    marginBottom: 10,
  },
  urlInput: {
    fontFamily: '\'Georgia\', serif',
    marginRight: 10,
    padding: 3,
  },
  editor: {
    border: '1px solid #ccc',
    cursor: 'text',
    minHeight: 80,
    padding: 10,
  },
  button: {
    marginTop: 10,
    textAlign: 'center',
  },
  link: {
    color: '#3b5998',
    textDecoration: 'underline',
    position:"relative"
  }
};


const colorStyleMap = {
   red: {
     color: 'rgba(255, 0, 0, 1.0)',
   },
   orange: {
     color: 'rgba(255, 127, 0, 1.0)',
   },
   yellow: {
     color: 'rgba(180, 180, 0, 1.0)',
   },
   green: {
     color: 'rgba(0, 180, 0, 1.0)',
   },
   blue: {
     color: 'rgba(0, 0, 255, 1.0)',
   },
   indigo: {
     color: 'rgba(75, 0, 130, 1.0)',
   },
   violet: {
     color: 'rgba(127, 0, 255, 1.0)',
   },
 };


const AbstractEditor = () => {

  const decorator = new CompositeDecorator([
    {
      strategy: findLinkEntities,
      component: Link
    }
  ]);

  const [editorState, setEditorState] = useState(EditorState.createEmpty(decorator));
  const [showUrlInput, setShowUrlInput] = useState(false);
  const [urlValue, setUrlValue] = useState("");

  const onChange = (newState) => {
    setEditorState(newState);
  };

  const handleInlineToggle = (e, inlineStyle) => {
    e.preventDefault();
    onChange(RichUtils.toggleInlineStyle(editorState, inlineStyle));
  };

  const logState = () => {
    const content = editorState.getCurrentContent();
    console.log(convertToRaw(content));
  };

  const promptForLink = e => {
    e.preventDefault();
    const selection = editorState.getSelection();
    if(!selection.isCollapsed()){
      const contentState = editorState.getCurrentContent();
      const startKey = editorState.getSelection().getStartKey();
      const startOffset = editorState.getSelection().getStartOffset();
      const blockWithLinkAtBeginning = contentState.getBlockForKey(startKey);
      const linkKey = blockWithLinkAtBeginning.getEntityAt(startOffset);

      let url = "";
      if(linkKey){
        const linkInstance = contentState.getEntity(linkKey);
        url = linkInstance.getData().url;
      }

      setUrlValue(url);
      setShowUrlInput(true);
    }
  };

  const confirmLink = e => {
    e.preventDefault();
    const contentState = editorState.getCurrentContent();
    const contentStateWithEntity = contentState.createEntity("LINK", "MUTABLE", {url: urlValue});
    const entityKey = contentStateWithEntity.getLastCreatedEntityKey();
    const newEditorState = EditorState.set(editorState, {currentContent: contentStateWithEntity});
    setEditorState(RichUtils.toggleLink(newEditorState, newEditorState.getSelection(), entityKey));
  };

  const onLinkInputKeyDown = e => {
    if(e.which === 13){
      confirmLink(e);
    }
  };

  const removeLink = e => {
    e.preventDefault();

    const selectionState = editorState.getSelection();
    if(!selectionState.isCollapsed()){
      setEditorState(RichUtils.toggleLink(editorState, selectionState, null));
    }
  };


  const myKeyBindingFn = e => {

    if (e.keyCode === 9) {
      e.preventDefault();
      return 'tab';
    }
    return getDefaultKeyBinding(e);
  }

  const handleKeyCommand = (command, editorState) => {

    if(command === "BOLD" || command === "ITALIC" || command === "UNDERLINE" || command === "STRIKETHROUGH"){
      const newState = RichUtils.handleKeyCommand(editorState, command);
      if(newState){
        onChange(newState);
        return "handled";
      }
    }else{

      if(command === "tab"){
        const contentState = editorState.getCurrentContent();
        const selection = editorState.getSelection();
        const newContentState = Modifier.insertText(contentState, selection, "\t");
        const newEditorState = EditorState.push(editorState, newContentState, "insert-characters");
        setEditorState(newEditorState);//todo
        return "handled";
      }
    }

    return "not-handled";
  };

  const onUrlChange = (e) => {
    setUrlValue(e.target.value);
  };

  const setBlock = (e, blockType) => {
    e.preventDefault();

    const newState = RichUtils.toggleBlockType(editorState, blockType);
    console.log(newState);
    onChange(newState);
  };



  const handleColor = (e, inputColor) => {
    e.preventDefault();

    const contentState = editorState.getCurrentContent();
    const selection = editorState.getSelection();

    // Object.keys()로 {green, yellow, orange, ...} 배열을 받아오고 [].reduce를 진행.
    // 초기값으로 contentState를 주고 이를 기반으로 하여 color 들을 loop돌려서 color들을 제거한다.
    // 루프를 돌릴 떄 마다, 즉 인라인 스타일을 제거할 때마다 Modifier.removeInlineStyle() 메서드에서
    // 반환되는 새로운 상태 시점인 contentState가 다음 루프의 입력값으로 쓰인다.
    // 그렇게 루프가 모두 끝나면 마지막 루프에서 반환된 contentState가 "newContentState"에 담긴다.
    const nextContentState = Object.keys(colorStyleMap).reduce((contentState, color) => {
      return Modifier.removeInlineStyle(contentState, selection, color)
    }, contentState);

    // Redo/Undo 히스토리에 저장
    let nextEditorState = EditorState.push(editorState, nextContentState, "change-inline-style");

    // 텍스트를 작성하면서 컬러를 변경해오면 currentStyle변수에 컬러를 변경한 이력(갯수)이 들어가는 듯
    const currentStyle = editorState.getCurrentInlineStyle();
    console.log(currentStyle);

    // 텍스트 범위지정 없으면
    if(selection.isCollapsed()){
      let a= 1;
      nextEditorState = currentStyle.reduce((state, colorStyle) => {
        //console.log("시도 횟수: " + a++);
        // 칼라를 주황,초록 번갈아가면서 세팅하고 글을 입력하면 reduce 루프가 2번 도는 것을 확인함
        // 처음에 주황, 그 후에 초록 값이 루프로 돌았다면
        // 처음에는 주황이 입력되고 그 상태에 덮어씌우기로 초록이 입력되고 최종적으로 최신 스냅샷엔 초록이 세팅됨.
        return RichUtils.toggleInlineStyle(state, colorStyle);
      }, nextEditorState);
    }

    // editorState.getCurrentInlineStyle().has(colorMap)
    if(!currentStyle.has(inputColor)){
      nextEditorState = RichUtils.toggleInlineStyle(nextEditorState, inputColor);
      setEditorState(nextEditorState);
    }
  };

  return (
    <div>
      <button onMouseDown={(e) => handleInlineToggle(e, "BOLD")}>굵게</button>
      <button onMouseDown={(e) => handleInlineToggle(e, "ITALIC")}>기울게</button>
      <button onMouseDown={(e) => handleInlineToggle(e, "UNDERLINE")}>밑줄</button>
      <button onMouseDown={(e) => handleInlineToggle(e, "STRIKETHROUGH")}>가로선</button>
      <button onMouseDown={(e) => setBlock(e, "header-one")}>헤더</button>
      <button onMouseDown={(e) => setBlock(e, "paragraph")}>문단</button>
      <button onMouseDown={(e) => handleColor(e, "green")}>초록</button>
      <button onMouseDown={(e) => handleColor(e, "orange")}>주황</button>
      <button onMouseDown={promptForLink}>링크추가</button>
      <button onMouseDown={logState}>로그</button>

      {showUrlInput &&
        <div style={styles.urlInputContainer}>
          <input
            onChange={onUrlChange}
            style={styles.urlInput}
            type="text"
            value={urlValue}
            onKeyDown={onLinkInputKeyDown}
           />
           <button onMouseDown={confirmLink}>적용</button>
        </div>
      }


      <div className="divider"></div>

      <Editor
        editorState={editorState}
        onChange={onChange}
        handleKeyCommand={handleKeyCommand}
        placeholder="텍스트 입력.."
        keyBindingFn={myKeyBindingFn}
        customStyleMap={colorStyleMap}
      />
    </div>
  );
};

const findLinkEntities = (contentBlock, callback, contentState) => {
  contentBlock.findEntityRanges(character => {
    const entityKey = character.getEntity();
    return (
      entityKey !== null && contentState.getEntity(entityKey).getType() === "LINK"
    );
  }, callback);
};


const Link = props => {
  const {url} = props.contentState.getEntity(props.entityKey).getData();
  const [show, setShow] = useState(false);


  const handleOpen = () => {
    setShow(true);
  };

  const handleClose = () => {
    setShow(false);
  };

  return (
    <>
      <a href={url} alt={url} style={styles.link} target="_blank" onClick={handleOpen}>
        {props.children}
      </a>
      {show === true && (
        <div className="tooltip" contentEditable="false">
          <a href={url} alt={url} target="_blank">{props.children}</a>
          <button onClick={handleClose}>닫기</button>
        </div>
      )}
    </>
  );
};

// contentEditable 경고 지우는 코드 복붙..
console.error = (function() {
    let error = console.error

    return function(exception) {
        if ((exception + '').indexOf('Warning: A component is `contentEditable`') != 0) {
            error.apply(console, arguments)
        }
    }
})()

export default AbstractEditor;
