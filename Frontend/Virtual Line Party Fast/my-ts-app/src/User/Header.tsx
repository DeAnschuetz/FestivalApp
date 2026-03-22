import React, { useEffect, useState, useRef } from "react";
import { Button } from "@progress/kendo-react-buttons";
import styles from "./Modules/Header.module.css";
import { useNavigate } from "react-router-dom";
import { BasketItem } from "../Types";

interface HeaderProps {
  setPayisOpen: React.Dispatch<React.SetStateAction<boolean>>;
  credits: number;
  setOpenBasket: React.Dispatch<React.SetStateAction<boolean>>;
  orderBasket: {
    foodcourt_name: string;
    Items: BasketItem[];
  };
  openBasket: boolean;
  setClickedFoodCourt: React.Dispatch<React.SetStateAction<string>>;
}

function Header(props: HeaderProps) {
  const {
    setPayisOpen,
    credits,
    setOpenBasket,
    orderBasket,
    openBasket,
    setClickedFoodCourt,
  } = props;
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const navigate = useNavigate();
  const loginNr = localStorage.getItem("currentUser");

  const dropdownRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        dropdownRef.current &&
        !dropdownRef.current.contains(event.target as Node)
      ) {
        setIsDropdownOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [loginNr]);

  const handleLogout = () => {
    localStorage.removeItem("currentUser");
    navigate("/login");
  };

  return (
    <div className={styles.User}>
      <div className={styles.Wrapper}>
        <Button
          type="button"
          fillMode={"flat"}
          iconClass="fa-regular fa-envelope"
          size={"medium"}
          style={{ marginRight: "6px" }}
        />
        <div className={styles.BasketWrapper}>
          <Button
            type="button"
            fillMode={"flat"}
            iconClass="fa fa-basket-shopping"
            size={"medium"}
            onClick={() => {
              setOpenBasket(!openBasket);
              setClickedFoodCourt("");
            }}
          />
          {orderBasket.Items.length !== 0 && (
            <div className={styles.FullBasket}>{orderBasket.Items.length}</div>
          )}
        </div>
        <div className={styles.Credits}>
          <i className="fa fa-money-bills"></i>
          <div className={styles.Price}>{credits} €</div>
          <Button
            type="button"
            fillMode={"outline"}
            iconClass="fa fa-plus"
            style={{ borderRadius: "100%" }}
            size={"small"}
            onClick={() => setPayisOpen(true)}
          />
        </div>
      </div>

      <div className={styles.UserWrapper} ref={dropdownRef}>
        <Button
          type="button"
          fillMode={"flat"}
          iconClass="fa fa-bars"
          size={"medium"}
          style={{ marginRight: "6px" }}
          onClick={() => setIsDropdownOpen((prev) => !prev)}
        />
        <i className="fa-regular fa-user"></i>
        <div className={styles.LogInNr}>{loginNr}</div>

        {isDropdownOpen && (
          <div className={styles.DropdownMenu}>
            <Button
              type="button"
              fillMode="flat"
              onClick={handleLogout}
              size={"small"}
            >
              <div className={styles.ButtonContent}>
                Log Out
                <i
                  className="fa fa-arrow-right-from-bracket"
                  style={{ marginLeft: "5px" }}
                ></i>
              </div>
            </Button>
          </div>
        )}
      </div>
    </div>
  );
}

export default Header;
