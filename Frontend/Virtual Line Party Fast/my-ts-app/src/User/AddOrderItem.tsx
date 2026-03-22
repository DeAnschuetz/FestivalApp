import React, { useEffect, useState } from "react";
import styles from "./Modules/AddOrderItem.module.css";
import { Dialog, DialogActionsBar } from "@progress/kendo-react-dialogs";
import InputElement from "../Components/InputElement";
import { Button } from "@progress/kendo-react-buttons";
import { FoodCourt, Product } from "../Api/ffb/types";
import { getProductsByFoodCourtId } from "../Api/ffb";

interface AddOrderItemProps {
  clickedItem: any;
  setOpenAddItemDialog: React.Dispatch<React.SetStateAction<boolean>>;
  currentFoodCourt: FoodCourt | undefined;
  setOrderBasket: React.Dispatch<React.SetStateAction<any>>;
}

function AddOrderItem(props: AddOrderItemProps) {
  const {
    clickedItem,
    setOpenAddItemDialog,
    setOrderBasket,
    currentFoodCourt,
  } = props;
  const [itemCount, setItemCount] = useState<string | number>(1);
  const [extraText, setExtraText] = useState("");
  const [selectedSubItems, setSelectedSubItems] = useState<any>({});
  const [products, setProducts] = useState<Product[]>([]);


  
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
  
  console.log("clickedItem: ", clickedItem);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const newItem = {
      name: clickedItem.name,
      extra: extraText,
      amount: itemCount,
      subItems: clickedItem.subItems?.map((subItem: any, index: number) => {
        const selected = selectedSubItems[index];
        return {
          name: selected || subItem.name,
        };
      }),
    };

    setOrderBasket((prev: any) => {
      if (!prev || !prev.Items || prev.Items.length === 0) {
        // Erster Eintrag
        return {
          foodcourt_name: currentFoodCourt?.name|| "",
          Items: [newItem],
        };
      } else if (prev.foodcourt_name === currentFoodCourt?.name) {
        // Weitere Items vom gleichen Stand
        return {
          ...prev,
          Items: [...prev.Items, newItem],
        };
      } else {
        return prev; // keine Änderung
      }
    });

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
            type={"number"}
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
