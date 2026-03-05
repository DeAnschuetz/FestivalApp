import React, { useState } from "react";
import styles from "./LogInContainer.module.css";
import InputElement from "../Components/InputElement";
import { Checkbox } from "@progress/kendo-react-inputs";
import { Button } from "@progress/kendo-react-buttons";
import { useNavigate } from "react-router-dom";

function RegisterContainer() {
  const [loginNr, setLoginNr] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  return (
    <div className={styles.Container}>
      <div className={styles.Title}>Create Account</div>
      <InputElement
        label="Login-Nr."
        editorId="login_nr"
        value={loginNr}
        onChange={setLoginNr}
        wrapperStyle={{ marginBottom: "10px" }}
        labelStyle={{ width: "100% " }}
        inputStyle={{ height: "36px" }}
      />
      <InputElement
        label="Password"
        editorId="password"
        value={password}
        onChange={setPassword}
        wrapperStyle={{ marginBottom: "10px" }}
        labelStyle={{ width: "100% " }}
        inputStyle={{ height: "36px" }}
        type="password"
      />
      <InputElement
        label="Confirm password"
        editorId="confirm_password"
        value={password}
        onChange={setPassword}
        wrapperStyle={{ marginBottom: "16px" }}
        labelStyle={{ width: "100% " }}
        inputStyle={{ height: "36px" }}
        type="password"
      />
      <Button
        style={{
          margin: "12px 0px",
          backgroundColor: "#2c2c2c",
          color: "white",
          height: "36px",
          fontSize: "large",
        }}
      >
        Create Account
      </Button>
      <Button
        style={{
          margin: "6px 0px 16px 0px",
          backgroundColor: "#2c2c2c",
          color: "white",
          height: "36px",
          fontSize: "large",
        }}
        onClick={() => navigate("/")}
      >
        Login
      </Button>
    </div>
  );
}

export default RegisterContainer;
