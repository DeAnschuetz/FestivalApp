import React, { useState } from "react";
import styles from './Modules/LogInContainer.module.css';
import InputElement from "../Components/InputElement";
import { Checkbox } from "@progress/kendo-react-inputs";
import { Button } from "@progress/kendo-react-buttons";
import { useNavigate } from "react-router-dom";

function LogInContainer() {
  const [loginNr, setLoginNr] = useState("");
  const [password, setPassword] = useState("");
  const [rememberMe, setRememberMe] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault(); // verhindert das Standard-Formular-Reload

    try {
      const response = await fetch("http://10.45.129.22:8080/account/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({ loginNr, password }),
      });

      if (!response.ok) {
        const text = await response.headers
        console.log('text: ', text);
        // throw new Error(text || "Login fehlgeschlagen");
      }

      // Login erfolgreich, z.B. Weiterleitung
      console.log("Login erfolgreich!");
      console.log("text",  await response)
      navigate("/user_view");
    } catch (err: any) {
      console.error(err);
      alert(err.message);
    }
  };

  return (
    <div className={styles.Container}>
      <div className={styles.Title}>Login</div>
      <form onSubmit={handleSubmit}>
        <InputElement
          label="Login-Nr."
          editorId="login_nr"
          value={loginNr}
          onChange={setLoginNr}
          wrapperStyle={{ marginBottom: "10px" }}
          labelStyle={{ width: "100%" }}
          inputStyle={{ height: "36px" }}
        />
        <InputElement
          label="Password"
          editorId="password"
          value={password}
          onChange={setPassword}
          wrapperStyle={{ marginBottom: "16px" }}
          labelStyle={{ width: "100%" }}
          inputStyle={{ height: "36px" }}
          type="password"
        />
        <div className={styles.Checkbox}>
          <Checkbox
            label="Remember me"
            checked={rememberMe}
            onChange={(e) => setRememberMe(e.value)}
          />
        </div>
        <Button
          type="submit"
          style={{
            margin: "12px 0px",
            backgroundColor: "#2c2c2c",
            color: "white",
            height: "36px",
            fontSize: "large",
            width: "100%"
          }}
        >
          Login
        </Button>
      </form>
      <Button
        style={{
          margin: "6px 0px 16px 0px",
          backgroundColor: "#2c2c2c",
          color: "white",
          height: "36px",
          fontSize: "large",
        }}
        onClick={() => navigate("/register")}
      >
        Create Account
      </Button>
    </div>
  );
}

export default LogInContainer;