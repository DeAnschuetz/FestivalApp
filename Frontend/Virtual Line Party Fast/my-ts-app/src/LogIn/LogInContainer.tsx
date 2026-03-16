import React, { useState } from "react";
import styles from "./Modules/LogInContainer.module.css";
import InputElement from "../Components/InputElement";
import { Checkbox } from "@progress/kendo-react-inputs";
import { Button } from "@progress/kendo-react-buttons";
import { useNavigate } from "react-router-dom";
import { users } from "../Data";
import { useAuth } from "../Auth/AuthContext";

function LogInContainer() {
  const [loginNr, setLoginNr] = useState("");
  const [password, setPassword] = useState("");
  const [rememberMe, setRememberMe] = useState(
    localStorage.getItem("rememberMe") === "true",
  );
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    setError("");

    const registeredUser = users.find((u) => u.login_Nr === loginNr);

    if (!registeredUser) {
      setError("Not registered yet. \nPlease Register first.");
      return;
    }

    const user = users.find(
      (u) => u.login_Nr === loginNr && u.password === password,
    );

    if (!user) {
      setError("Login number or password is incorrect.");
      return;
    }

    if (rememberMe) {
      localStorage.setItem("rememberedPassword_" + loginNr, password);
    } else {
      localStorage.removeItem("rememberedPassword_" + loginNr);
    }

    login(user.type);

    switch (user.type) {
      case "ADMIN":
        navigate("/admin_view");
        break;

      case "GUEST":
        navigate("/user_view");
        break;

      case "FOOD_COURT_WORKER":
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
            lineHeight: '1.3',
            display: 'flex',
            padding: '0px 0px 0px 6px'
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
            setLoginNr(value);

            const saved = localStorage.getItem("rememberedPassword_" + value);
             setError("");

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
          inputStyle={{ height: "36px",  border: error ? "1px solid red" : undefined, }}
        />
        <InputElement
          label="Password"
          editorId="password"
          value={password}
          onChange={setPassword}
          wrapperStyle={{ marginBottom: "16px" }}
          labelStyle={{ width: "100%" }}
          inputStyle={{ height: "36px", border: error ? "1px solid red" : undefined, }}
          type="password"
        />
        <div className={styles.Checkbox}>
          <Checkbox
            label="Remember me"
            checked={rememberMe}
            onChange={(e) => {
              const checked = e.value;
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
