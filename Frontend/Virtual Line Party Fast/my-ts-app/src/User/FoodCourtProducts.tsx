import React, { useState } from "react";
import styles from "./Modules/FoodCourtProducts.module.css";
import { BasketItem, FoodCourt } from "../Types";
import { Button } from "@progress/kendo-react-buttons";
import AddOrderItem from "./AddOrderItem";

interface FoodCourtProductsProps {
  currentFoodCourt: FoodCourt | undefined;
  setClickedFoodCourt: React.Dispatch<React.SetStateAction<string>>;
  setOrderBasket: React.Dispatch<React.SetStateAction<any>>;
  orderBasket: { foodcourt_name: string; Items: BasketItem[] };
}

function FoodCourtProducts(props: FoodCourtProductsProps) {
  const { currentFoodCourt, setClickedFoodCourt, setOrderBasket, orderBasket } =
    props;
  console.log("orderBasket: ", orderBasket);
  const [openAddItemDialog, setOpenAddItemDialog] = useState(false);
  const [clickedItem, setClickedItem] = useState({});
  const [errorText, setErrorText] = useState("");

  const onAddClick = (item: any) => {
    if (
      orderBasket.Items.length > 0 &&
      orderBasket.foodcourt_name !== currentFoodCourt?.name
    ) {
      // Fehler anzeigen, Dialog nicht öffnen
      setErrorText(
        "Parallel bei unterschiedlichen Ständen bestellen ist nicht möglich",
      );
      setOpenAddItemDialog(false);
    } else {
      setClickedItem(item);
      setOpenAddItemDialog(true);
      setErrorText(""); // Fehler zurücksetzen, falls vorher gesetzt
    }
  };
  return (
    <div className={styles.FoodCourtProducts}>
      <div className={styles.FlexWrapper}>
        <div className={styles.CourtName}>{currentFoodCourt?.name}</div>
        <Button
          type="button"
          fillMode="flat"
          iconClass="fa-regular fa-circle-xmark"
          size="small"
          style={{ fontSize: "24px" }}
          onClick={() => setClickedFoodCourt("")}
        />
      </div>
      <div className={styles.ProductsContainer}>
        {currentFoodCourt?.products.map((product) => (
          <div>
            <div className={styles.ProductRow}>
              <div className={styles.Wrapper}>
                <i className={product.icon}></i>
                <div className={styles.Name}>{product.name}</div>
              </div>
              <div className={styles.Wrapper}>
                <div className={styles.Price}>{product.price} €</div>
                <Button
                  type="button"
                  fillMode={"outline"}
                  iconClass="fa fa-plus"
                  style={{ borderRadius: "100%" }}
                  size={"small"}
                  onClick={() => onAddClick(product)}
                />
              </div>
            </div>
            <div className={styles.SubItemsWrapper}>
              {product.subItems?.map((item) => (
                <div>- {item.name}</div>
              ))}
            </div>
          </div>
        ))}
      </div>
      {errorText && (
        <div className={styles.ErrorText}>
          <b>Bestellung im Warenkorb zuerst abschließen. </b> Parallel bei
          unterschiedlichen Ständen bestellen ist nicht möglich
        </div>
      )}
      <div className={styles.WaitingTimeContainer}>
        <div>Geschätzte Wartezeit</div>
        <div className={styles.WaitingTime}>
          <i className="fa-regular fa-clock"></i>
          <div className={styles.Time}>
            {currentFoodCourt?.avg_waiting_time} Min
          </div>
        </div>
      </div>
      {openAddItemDialog && (
        <AddOrderItem
          clickedItem={clickedItem}
          setOpenAddItemDialog={setOpenAddItemDialog}
          currentFoodcourt={currentFoodCourt}
          setOrderBasket={setOrderBasket}
        />
      )}
    </div>
  );
}

export default FoodCourtProducts;
