import React, { useEffect, useState } from "react";
import styles from "./Modules/LogInContainer.module.css";
import InputElement from "../Components/InputElement";
import { Checkbox } from "@progress/kendo-react-inputs";
import { Button } from "@progress/kendo-react-buttons";
import { useNavigate } from "react-router-dom";
import { login, LoginResult } from "../Api/ffb";
import { AccountType } from "../Api/generated/ffbAPI.schemas";
import { getAccountTypeForRouting } from "./loginHelpers";

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

function LogInContainer() {
  const [loginNr, setLoginNr] = useState("");
  const [password, setPassword] = useState("");
  const [rememberMe, setRememberMe] = useState(
    localStorage.getItem("rememberMe") === "true",
  );
  const [error, setError] = useState("");
  const navigate = useNavigate();
  
  useEffect(() => {
    setLoginNr("");
    setPassword("");
    setError("");
  }, []);
  
  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    
    const isValidLoginNr = (value: string) => /^[AFV]-\d{3}-\d{3}-\d{3}$/.test(value);
    if (!isValidLoginNr(loginNr)) {
      setError("Invalid Login Nr.");
      return;
    }
    e.preventDefault();
    setError("");

    const data: LoginResult = await login({loginNr, password});


    localStorage.setItem("currentUser", data.loginNr);

    if (rememberMe) {
      localStorage.setItem("rememberedPassword_" + loginNr, password);
    } else {
      localStorage.removeItem("rememberedPassword_" + loginNr);
    }
    const type: AccountType = getAccountTypeForRouting(data.loginNr);
  
    switch (type) {
      case AccountType.ADMIN:
        navigate("/admin_view");
        break;

      case AccountType.GUEST:
        navigate("/user_view");
        break;

      case AccountType.FOOD_COURT_WORKER:
        navigate("/foodcourt_view");
        break;
    }
  };

  return (
    <div className={styles.Container}>
      <div className={styles.Title}>Login</div>
      {error && (
        <div
          style={{
            color: "red",
            margin: "3px 0px 16px 0px",
            fontSize: "14px",
            whiteSpace: "pre-line",
            lineHeight: "1.3",
            display: "flex",
            padding: "0px 0px 0px 6px",
          }}
        >
          {error}
        </div>
      )}
      <form onSubmit={handleSubmit} autoComplete="off">
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
          wrapperStyle={{ marginBottom: "16px" }}
          labelStyle={{ width: "100%" }}
          inputStyle={{
            height: "36px",
            border: error ? "1px solid red" : undefined,
          }}
          type="password"
          maxLength={13}
        />

        <div className={styles.Checkbox}>
          <Checkbox
            label="Remember me"
            checked={rememberMe}
            onChange={(e) => {
              const checked = !!e.value;
              setRememberMe(checked);

              localStorage.setItem("rememberMe", String(checked));

              if (!checked) {
                localStorage.removeItem("rememberedPassword_" + loginNr);
                setPassword("");
              }
            }}
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
            width: "100%",
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