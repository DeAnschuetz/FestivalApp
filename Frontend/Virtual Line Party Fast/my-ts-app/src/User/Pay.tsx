import React, { useState } from "react";
import styles from "./Modules/Pay.module.css";
import { Button } from "@progress/kendo-react-buttons";
import InputElement from "../Components/InputElement";
import { addCredit, getCredit } from "../Api/ffb/creditApi";
import { Credit } from "../Api/ffb/types";
import { CreditAddRequest } from "../Api/generated/ffbAPI.schemas";

interface PayProps {
  setPayisOpen: React.Dispatch<React.SetStateAction<boolean>>;
  currentUser: string;
  onCreditsUpdate?: (credits: number) => void;
}

function Pay({ setPayisOpen, currentUser, onCreditsUpdate }: PayProps) {
  const [selectedPayment, setSelectedPayment] = useState<string>("");
const [betrag, setBetrag] = useState<number | "">("");
  const [blz, setBlz] = useState<string>("");
  const [iban, setIban] = useState<string>("");
  const [submitted, setSubmitted] = useState(false);

  const paymentOptions = [
    { id: "visa", icon: "fa-brands fa-cc-visa" },
    { id: "paypal", icon: "fa-brands fa-paypal" },
    { id: "applepay", icon: "fa-brands fa-apple-pay" },
    { id: "mastercard", icon: "fa-brands fa-cc-mastercard" },
  ];

  const isCardPayment = ["visa", "mastercard"].includes(selectedPayment);

  const validation = {
    betrag: submitted && !betrag,
    payment: submitted && !selectedPayment,
    blz: submitted && isCardPayment && !blz,
    iban: submitted && isCardPayment && !iban,
  };

  const handleSubmit = async (e: React.FormEvent) => {
    try {
      e.preventDefault();
      setSubmitted(true);
  
      const isValid =
        selectedPayment &&
        betrag &&
        (!isCardPayment || (blz && iban));
      if (!isValid) return;
  
      const credit: Credit = await addCredit({amount: betrag} as CreditAddRequest);
      console.log(credit);
      onCreditsUpdate?.(credit.credit);
  
      setPayisOpen(false);
    } catch (error) {
      
    }
  };
  const getIconClasses = (optionId: string) => {
    return [
      paymentOptions.find((p) => p.id === optionId)?.icon,
      styles.IconRadio,
      selectedPayment === optionId ? styles.Active : "",
      validation.payment ? styles.InavlidPayment : "",
    ]
      .filter(Boolean)
      .join(" ");
  };

  return (
    <div className={styles.Pay}>
      <div className={styles.CloseBtnContainer}>
        <Button
          type="button"
          fillMode="flat"
          iconClass="fa-regular fa-circle-xmark"
          size="small"
          style={{ fontSize: "24px" }}
          onClick={() => setPayisOpen(false)}
        />
      </div>

      <form onSubmit={handleSubmit}>
        <div className={styles.Header}>
          <div className={styles.Banner}>Load Credits</div>
          <div className={styles.Icon}>
            <i className="fa fa-hand-holding-dollar"></i>
          </div>
        </div>

        <div className={styles.Line}></div>

        <div className={styles.ZahlungsmethodeText}>Zahlungsmethode</div>
        <div className={styles.RadioGroup}>
          {paymentOptions.map((option) => (
            <div
              key={option.id}
              className={styles.RadioOption}
              onClick={() => setSelectedPayment(option.id)}
            >
              <i className={getIconClasses(option.id)}></i>
            </div>
          ))}
        </div>

        {isCardPayment && (
          <>
            <div className={styles.Text}>Karteninfo</div>
            <div className={styles.FlexWrapper}>
              <InputElement
                label="BLZ"
                editorId="blz"
                value={blz}
                onChange={setBlz}
                labelStyle={{ width: "100px" }}
                inputStyle={{
                  width: "100px",
                  border: validation.blz ? "1px solid red" : undefined,
                }}
                wrapperStyle={{ marginRight: "20px" }}
              />
              <InputElement
                label="IBAN"
                editorId="iban"
                value={iban}
                onChange={setIban}
                inputStyle={{
                  border: validation.iban ? "1px solid red" : undefined,
                }}
              />
            </div>
            {(validation.blz || validation.iban) && (
              <div className={styles.ErrorText}>Required</div>
            )}
          </>
        )}

        <InputElement
          label="Betrag"
          editorId="betrag"
          type="number"
          value={betrag}
          onChange={(val: string) => setBetrag(val === "" ? "" : Number(val))}
          inputStyle={{
            border: validation.betrag ? "1px solid red" : undefined,
          }}
        />
        {validation.betrag && <div className={styles.ErrorText}>Required</div>}
        <div className={styles.PayBtnContainer}>
          <Button className={styles.PayBtn} type="submit">
            Bezahlen
          </Button>
        </div>
      </form>
    </div>
  );
}

export default Pay;
