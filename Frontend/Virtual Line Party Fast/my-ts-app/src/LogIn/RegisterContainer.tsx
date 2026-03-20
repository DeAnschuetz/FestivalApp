import { useState } from "react";
import styles from "./LogInContainer.module.css";
import InputElement from "../Components/InputElement";
import { Button } from "@progress/kendo-react-buttons";
import { useNavigate } from "react-router-dom";
import { login } from "../Api/ffb";
import { LoginRequest } from "../Api/generated/ffbAPI.schemas";
import { getAccountTypeForRouting } from "./loginHelpers";

function RegisterContainer() {
  const [loginNr, setLoginNr] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  const [error, setError] = useState("");

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");

    const loginRequest: LoginRequest = {
      loginNr,
      password,
    };

    try {
      const loggedInUser = await login(loginRequest);
      const userType = getAccountTypeForRouting(loggedInUser.loginNr ?? loginNr);

      switch (userType) {
        case "ADMIN":
          navigate("/admin_view");
          break;

        case "FOOD_COURT_WORKER":
          navigate("/foodcourt_view");
          break;

        case "GUEST":
        default:
          navigate("/user_view");
          break;
      }
    } catch (err) {
      const message =
        err instanceof Error ? err.message : "Login failed. Please try again.";
      setError(message);
    }
  };

  return (
    <div className={styles.Container}>
      <div className={styles.Title}>Create Account</div>
      <form onSubmit={handleSubmit}>
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
