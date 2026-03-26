import React, { useEffect, useState } from "react";
import styles from "./LogInContainer.module.css";
import InputElement from "../Components/InputElement";
import { Button } from "@progress/kendo-react-buttons";
import { useNavigate } from "react-router-dom";
import { LoginRequest, RegisterRequest } from "../Api/generated/ffbAPI.schemas";
import { getAccountTypeForRouting } from "./loginHelpers";
import { apiLogin, register } from "../Api/ffb/accountApi";

const formatLoginNr = (value: string): string => {
  const upper = value.toUpperCase();

  const firstCharMatch = upper.match(/[AFV]/);
  const firstChar = firstCharMatch ? firstCharMatch[0] : "";

  const digits = upper.replace(/\D/g, "").slice(0, 9);

  if (!firstChar) {
    return digits ? "" : "";
  }

  const parts = [];
  if (digits.length > 0) parts.push(digits.slice(0, 3));
  if (digits.length > 3) parts.push(digits.slice(3, 6));
  if (digits.length > 6) parts.push(digits.slice(6, 9));

  return [firstChar, ...parts].join("-");
};

function RegisterContainer() {
  const [loginNr, setLoginNr] = useState("");
  const [password, setPassword] = useState("");
  const [rememberMe, setRememberMe] = useState(
    localStorage.getItem("rememberMe") === "true",
  );
  const [confirmPassword, setConfirmPassword] = useState("");
  const navigate = useNavigate();
  const [error, setError] = useState("");

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");

    const registerRequest: RegisterRequest = {
      loginNr,
      password,
    };

    try {
      await register(registerRequest);

    } catch (err) {
      
    }
  };

  useEffect(() => {
    setLoginNr("");
    setPassword("");
    setConfirmPassword("");
    setError("");
  }, []);

  return (
    <div className={styles.Container}>
      <div className={styles.Title}>Create Account</div>

      {error && (
        <div
          style={{
            color: "red",
            marginBottom: "12px",
          }}
        >
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit}>
        <InputElement
          label="Login-Nr."
          editorId="login_nr"
          value={loginNr}
          onChange={(value) => {
            const formattedValue = formatLoginNr(value);

            setLoginNr(formattedValue);
            setError("");

            const saved = localStorage.getItem("rememberedPassword_" + formattedValue);

            if (saved) {
              setPassword(saved);
              setRememberMe(true);
            } else {
              setPassword("");
              setRememberMe(false);
            }
          }}
          wrapperStyle={{ marginBottom: "10px" }}
          labelStyle={{ width: "100%" }}
          inputStyle={{
            height: "36px",
            border: error ? "1px solid red" : undefined,
          }}
        />

        <InputElement
          label="Password"
          editorId="password"
          value={password}
          onChange={setPassword}
          wrapperStyle={{ marginBottom: "10px" }}
          labelStyle={{ width: "100%" }}
          inputStyle={{
            height: "36px",
            border: error ? "1px solid red" : undefined,
          }}
          type="password"
        />

        <InputElement
          label="Confirm password"
          editorId="confirm_password"
          value={confirmPassword}
          onChange={setConfirmPassword}
          wrapperStyle={{ marginBottom: "16px" }}
          labelStyle={{ width: "100%" }}
          inputStyle={{
            height: "36px",
            border: error ? "1px solid red" : undefined,
          }}
          type="password"
        />

        <Button
          type="submit"
          style={{
            margin: "12px 0px",
            backgroundColor: "#2c2c2c",
            color: "white",
            height: "36px",
            fontSize: "large",
            width: "100%",
          }}
        >
          Create Account
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
        onClick={() => navigate("/")}
      >
        Login
      </Button>
    </div>
  );
}

export default RegisterContainer;
