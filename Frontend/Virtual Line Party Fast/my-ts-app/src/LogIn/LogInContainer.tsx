import React, { useState } from "react";
import styles from "./Modules/LogInContainer.module.css";
import InputElement from "../Components/InputElement";
import { Checkbox } from "@progress/kendo-react-inputs";
import { Button } from "@progress/kendo-react-buttons";
import { useNavigate } from "react-router-dom";

type AccountType = "ADMIN" | "FOOD_COURT_WORKER" | "GUEST";
const API_BASE = "http://10.45.129.19:8080";

interface AccountResponse {
  id: string;
  loginNr: string;
  type?: AccountType;
  role?: AccountType;
}

function LogInContainer() {
  const [loginNr, setLoginNr] = useState("");
  const [password, setPassword] = useState("");
  const [rememberMe, setRememberMe] = useState(false);
  const navigate = useNavigate();

  const getAuthHeaders = (token: string) => ({
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  });

  const resolveTargetRoute = async (token: string): Promise<string> => {
    try {
      const accountResponse = await fetch(`${API_BASE}/account`, {
        method: "GET",
        headers: getAuthHeaders(token),
        credentials: "include",
      });

      if (accountResponse.ok) {
        const accountData = (await accountResponse.json()) as AccountResponse;
        const accountType = (accountData.type ?? accountData.role ?? "").toUpperCase();
        if (accountType === "FOOD_COURT_WORKER") {
          return "/food_court";
        } else if (accountType === "ADMIN") {
          return "/user_view";
        }
      }
    } catch (accountError) {
      console.warn("Account-Typ konnte nicht geladen werden", accountError);
    }

    try {
      const foodCourtProbe = await fetch(`${API_BASE}/food_court`, {
        method: "GET",
        headers: getAuthHeaders(token),
        credentials: "include",
      });

      if (foodCourtProbe.ok) {
        return "/food_court";
      }
    } catch (probeError) {
      console.warn("Food-Court Probe fehlgeschlagen", probeError);
    }

    return "/user_view";
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch(`${API_BASE}/account/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({ loginNr, password }),
      });

      if (!response.ok) {
        throw new Error("Login fehlgeschlagen");
      }

      const data = await response.json();
      localStorage.setItem("token", data.token);
      const targetRoute = await resolveTargetRoute(data.token);

      console.log("Login erfolgreich!");
      navigate(targetRoute);
      console.log("Navigiere zu:", targetRoute);
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
