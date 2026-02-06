import React, { useState } from "react";
import styles from "./LogInContainer.module.css";
import InputElement from "../Components/InputElement";
import { Checkbox } from "@progress/kendo-react-inputs";
import { Button } from "@progress/kendo-react-buttons";

function LogInContainer() {
  const [loginNr, setLoginNr] = useState("");
  const [password, setPassword] = useState("");

  return (
    <div className={styles.Container}>
      <div className={styles.Title}>Login</div>
      <InputElement
        label="Login-Nr."
        editorId="login_nr"
        value={loginNr}
        onChange={setLoginNr}
        wrapperStyle={{ marginBottom: "10px" }}
        labelStyle={{ width: "100% "}}
        inputStyle={{ height: "36px" }}
      />
      <InputElement
        label="Password"
        editorId="password"
        value={password}
        onChange={setPassword}
        wrapperStyle={{ marginBottom: "16px" }}
        labelStyle={{ width: "100% " }}
        inputStyle={{ height: "36px" }}
        type="password"
      />
      <div className={styles.Checkbox}>
        <Checkbox label="Remeber me" />
      </div>
      <Button
        style={{
          margin: "12px 0px",
          backgroundColor: "#2c2c2c",
          color: "white",
          height: "36px",
          fontSize: "large",
        }}
      >
        Login
      </Button>
      <Button
        style={{
          margin: "6px 0px 16px 0px",
          backgroundColor: "#2c2c2c",
          color: "white",
          height: "36px",
          fontSize: "large",
        }}
      >
        Create Account
      </Button>
    </div>
  );
}

export default LogInContainer;
