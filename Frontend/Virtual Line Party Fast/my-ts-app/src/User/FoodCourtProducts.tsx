import React, { useEffect, useState } from "react";
import styles from "./Modules/FoodCourtProducts.module.css";
import { BasketItem  } from "../Types";
import { Button } from "@progress/kendo-react-buttons";
import AddOrderItem from "./AddOrderItem";
import { FoodCourt, Product } from "../Api/ffb/types";
import { getProductsByFoodCourtId } from "../Api/ffb/productApi";

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
  const [products, setProducts] = useState<Product[]>([]);

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

  useEffect(() => {
      const loadFoodCourtData = async () => {
        try {
          if (currentFoodCourt) {
            const products: Product[] = await getProductsByFoodCourtId(currentFoodCourt?.id);
            setProducts(products);
          }
        } catch (error) {
          
        } 
        };
    
        void loadFoodCourtData();
  }, []);

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
        {products.map((product) => (
          <div>
            <div className={styles.ProductRow}>
              <div className={styles.Wrapper}>
                <i className={product.symbolIdentifier}></i>
                <div className={styles.Name}>{product.displayName}</div>
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
              {product.subProducts?.map((item) => (
                <div>- {item.displayName}</div>
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
            {currentFoodCourt?.waitingTime} Min
          </div>
        </div>
      </div>
      {openAddItemDialog && (
        <AddOrderItem
          clickedItem={clickedItem}
          setOpenAddItemDialog={setOpenAddItemDialog}
          currentFoodCourt={currentFoodCourt}
          setOrderBasket={setOrderBasket}
        />
      )}
    </div>
  );
}

export default FoodCourtProducts;
