import React, { useEffect, useState } from "react";
import styles from "./Modules/AddOrderItem.module.css";
import { Dialog, DialogActionsBar } from "@progress/kendo-react-dialogs";
import InputElement from "../Components/InputElement";
import { Button } from "@progress/kendo-react-buttons";
import { Cart, FoodCourt, Product } from "../Api/ffb/types";
import { apiAddCartItem } from "../Api/ffb";
import { CartItemCreationRequest } from "../Api/generated/ffbAPI.schemas";

interface AddOrderItemProps {
  clickedItem: any;
  products: Product[];
  setOpenAddItemDialog: React.Dispatch<React.SetStateAction<boolean>>;
  currentFoodCourt: FoodCourt | undefined;
  setCart: React.Dispatch<React.SetStateAction<any>>;
}

function AddOrderItem(props: AddOrderItemProps) {
  const {
    products,
    clickedItem,
    setOpenAddItemDialog,
    setCart,
    currentFoodCourt,
  } = props;
  const [itemCount, setItemCount] = useState<string | number>(1);
  const [extraText, setExtraText] = useState("");
  const [selectedSubItems, setSelectedSubItems] = useState<any>({});

  console.log("clickedItem: ", clickedItem);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const cartItemRequest: CartItemCreationRequest = {
      productId: clickedItem.id,
      itemCount: itemCount as number,
      extra: extraText
    }

    const addOrderItem = async () => {
      try {
        const newCart: Cart = await apiAddCartItem(cartItemRequest);
        setCart(newCart);
        
      } catch (error) {
      
      }
    }
    addOrderItem();
    setOpenAddItemDialog(false);
  };
  return (
    <Dialog
      title={<div className={styles.Title}>{clickedItem.name}</div>}
      width="320px"
      className="Dialog"
      onClose={() => setOpenAddItemDialog(false)}
    >
      <form onSubmit={handleSubmit}>
        <div className={styles.DialogBody}>
          <div>{clickedItem.name} der Betstellung hinzufügen</div>
          <InputElement
            label="Anzahl"
            editorId="anzahl"
            value={itemCount}
            onChange={setItemCount}
            labelStyle={{ width: "50%", color: "black" }}
            inputStyle={{ color: "black" }}
            wrapperStyle={{ margin: "12px 0px" }}
          />

          <div className={styles.OrderContext}>
            <div className={styles.Flex}>
              {clickedItem.sorts?.map((sort: any) => (
                <div className={styles.RadioGroup}>
                  <div className={styles.Option}>
                    <input
                      type="radio"
                      name={sort}
                      value={sort}
                      onChange={() =>
                        setSelectedSubItems((prev: any) => ({
                          ...prev,
                          sort,
                        }))
                      }
                    />
                    <div> {sort}</div>
                  </div>
                </div>
              ))}
            </div>
            {clickedItem.subItems?.map((subItem: any, index: number) => {
              // passendes Produkt anhand type finden
              const relatedProduct = products.find(
                (p: any) => p.type === subItem.type,
              );

              return (
                <div key={index} className={styles.SubItemContainer}>
                  <div className={styles.SubItemTitle}>{subItem.name}</div>

                  {/* FALL 1: Produkt hat sorts (z.B. Drink)
                  {relatedProduct?.sorts && (
                    <div className={styles.RadioGroup}>
                      {relatedProduct.sorts.map((sort: string, i: number) => (
                        <div key={i} className={styles.Option}>
                          <input
                            type="radio"
                            name={`subitem-${index}`}
                            value={sort}
                            onChange={() =>
                              setSelectedSubItems((prev: any) => ({
                                ...prev,
                                [index]: sort,
                              }))
                            }
                          />
                          <div> {sort}</div>
                        </div>
                      ))}
                    </div>
                  )} */}

                  {/* FALL 2: kein sorts → alle Produkte mit gleichem type anzeigen */}
                  {/* {!relatedProduct?.sorts && subItem.type && (
                    <div className={styles.RadioGroup}>
                      {products
                        .filter((p: any) => p.type === subItem.type)
                        .map((p: any, i: number) => (
                          <label key={i}>
                            <input
                              type="radio"
                              name={`subitem-${index}`}
                              value={p.name}
                              onChange={() =>
                                setSelectedSubItems((prev: any) => ({
                                  ...prev,
                                  [index]: p.name,
                                }))
                              }
                            />
                            {p.name}
                          </label>
                        ))}
                    </div>
                  )} */}
                </div>
              );
            })}
          </div>
          <div>
            <div className={styles.LabelText}>Sonderwünsche</div>
            <InputElement
              label=""
              editorId="anzahl"
              value={extraText}
              onChange={setExtraText}
              labelStyle={{ width: "80%", color: "black", padding: 0 }}
              inputStyle={{ color: "black", height: "60px" }}
              wrapperStyle={{ margin: "0px 0px" }}
            />
          </div>
        </div>

        <DialogActionsBar>
          <div className={styles.BtnContainer}>
            <div>
              <Button
                type="button"
                className={styles.CancelBtn}
                onClick={() => setOpenAddItemDialog(false)}
              >
                Cancel
              </Button>
            </div>
            <div>
              <Button type="submit" className={styles.ShareBtn}>
                Add
              </Button>
            </div>
          </div>
        </DialogActionsBar>
      </form>
    </Dialog>
  );
}

export default AddOrderItem;
