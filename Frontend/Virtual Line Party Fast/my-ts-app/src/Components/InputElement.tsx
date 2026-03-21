import React, { CSSProperties } from "react";
import { Input, InputChangeEvent } from "@progress/kendo-react-inputs";
import { FloatingLabel } from "@progress/kendo-react-labels";
import "@progress/kendo-theme-default/dist/all.css";

type InputElementProps = {
  label: string;
  editorId: string;
  value: string | number;
  onChange: (e: string) => void;
  wrapperStyle?: CSSProperties;
  inputStyle?: CSSProperties;
  labelStyle?: CSSProperties;
  type?: "text" | "password" | "number";
};

function InputElement(props: InputElementProps) {
  const { label, editorId, value, onChange, wrapperStyle, inputStyle, labelStyle,type } =
    props;

  return (
    <div style={wrapperStyle}>
      <FloatingLabel
        label={label}
        style={labelStyle}
        editorId={editorId}
        editorValue={value || ""}
        optional={false}
      >
      <input
        id={editorId}
        type={type ?? "text"}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        style={inputStyle}
      />
      </FloatingLabel>
    </div>
  );
}

export default InputElement;
