import React, { useEffect, useState } from "react";
import styles from "./Modules/FoodCourtTab.module.css";
import { Button } from "@progress/kendo-react-buttons";
import FoodCourtCard from "../Components/FoodCourtCard";
import { FoodCourt } from "../Api/ffb/types";
import { getAllFoodCourts } from "../Api/ffb/foodCourtApi";

interface FoodCourtTabProps {
  setIsFoodCourtOpen: React.Dispatch<React.SetStateAction<boolean>>;
  setClickedFoodCourt: React.Dispatch<React.SetStateAction<string>>;
  isOrdersOpen: boolean;
}

function FoodCourtTab(props: FoodCourtTabProps) {
  const { isOrdersOpen, setClickedFoodCourt, setIsFoodCourtOpen } = props;
  const [isOpen, setIsOpen] = useState(true);
  const [foodCourts, setFoodCourts] = useState<FoodCourt[]>([]);

  const showOpen = () => {
    setIsOpen(!isOpen);
    setIsFoodCourtOpen(isOpen);
  };

  useEffect(() => {
    const loadFoodCourtData = async () => {
        try {
          const foodCourts: FoodCourt[] = await getAllFoodCourts();
          setFoodCourts(foodCourts); 
        } catch (error) {
          
        }
      };
  
      void loadFoodCourtData();
  }, []);

  return (
    <div>
      <div className={styles.Header}>
        <Button
          type="button"
          fillMode={"flat"}
          iconClass={!isOpen ? "fa fa-angle-left" : "fa fa-angle-down"}
          size={"medium"}
          onClick={showOpen}
        ></Button>
        <div
          className={`${styles.BtnLabel}  ${isOpen ? styles.OpenLabel : ""}`}
        >
          Foodcourt
        </div>
      </div>
      {isOpen && (
        <div className={styles.Content}>
          <div
            className={`${styles.FoodCourtContainer} ${isOrdersOpen ? styles.BiggerContainer : ""}`}
          >
            {foodCourts
                .map((foodCourt) => (
                <FoodCourtCard
                  foodCourt={foodCourt}
                  setClickedFoodCourt={setClickedFoodCourt}
                />
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default FoodCourtTab;
