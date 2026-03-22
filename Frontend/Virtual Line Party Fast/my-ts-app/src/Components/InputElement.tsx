import React, { CSSProperties } from "react";
import { Input, InputChangeEvent } from "@progress/kendo-react-inputs";
import { FloatingLabel } from "@progress/kendo-react-labels";
import "@progress/kendo-theme-default/dist/all.css";

type InputElementProps = {
  label: string;
  editorId: string;
  value: string | number;
  onChange: (e: any) => void;
  wrapperStyle?: CSSProperties;
  inputStyle?: CSSProperties;
  labelStyle?: CSSProperties;
  type?: "text" | "password";
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
        <Input
          id={editorId}
          value={value || ""}
          type={type}
          style={inputStyle}
          onChange={(e: InputChangeEvent) => onChange(e.value ?? "")}
        />
      </FloatingLabel>
    </div>
  );
}

export default InputElement;
