import React, { useCallback, useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./StandPage.module.css";
import { FoodCourtResponse } from "../Api/generated/ffbAPI.schemas";
import { updateProductCount } from "../Api/ffb/productApi";

const API_BASE = "http://10.45.129.19:8080";

interface Product {
  id: string;
  price: number;
  displayName: string;
  symbolIdentifier: string;
  minimalWarning: number;
  productCount: number;
}

function StandPage() {
  const token = localStorage.getItem("token");
  const loginLabel = localStorage.getItem("loginNr") ?? "1234WP56-ZY09";
  const navigate = useNavigate();

  const [foodCourt, setFoodCourt] = useState<FoodCourtResponse | null>(null);
  const [products, setProducts] = useState<Product[]>([]);
  const [waitingTime, setWaitingTime] = useState<number>(15);
  const [editedCounts, setEditedCounts] = useState<Record<string, number>>({});
  const [error, setError] = useState<string>("");

  const authHeaders = useMemo(
    () => {
      const headers: Record<string, string> = {
        "Content-Type": "application/json",
      };

      if (token) {
        headers.Authorization = `Bearer ${token}`;
      }

      return headers;
    },
    [token],
  );

  const loadData = useCallback(async () => {
    setError("");

    try {
      const [foodCourtResponse, productsResponse] = await Promise.all([
        fetch(`${API_BASE}/food_court`, {
          method: "GET",
          headers: authHeaders,
          credentials: "include",
        }),
        fetch(`${API_BASE}/product/list`, {
          method: "GET",
          headers: authHeaders,
          credentials: "include",
        }),
      ]);

      if (!foodCourtResponse.ok) {
        throw new Error("Food Court konnte nicht geladen werden.");
      }

      if (!productsResponse.ok) {
        throw new Error("Produkte konnten nicht geladen werden.");
      }

      const foodCourtResult = (await foodCourtResponse.json()) as FoodCourtResponse;
      const productResult = (await productsResponse.json()) as Product[];

      setFoodCourt(foodCourtResult);
      setWaitingTime(foodCourtResult.waitingTime ?? 15);
      setProducts(productResult);
      setEditedCounts(
        productResult.reduce<Record<string, number>>((accumulator, product) => {
          accumulator[product.id] = product.productCount;
          return accumulator;
        }, {}),
      );
    } catch (loadError) {
      setError(loadError instanceof Error ? loadError.message : "Unbekannter Fehler");
    }
  }, [authHeaders]);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const updateWaitingTime = async () => {
    if (!foodCourt) {
      return;
    }

    const safeWaitingTime = Number.isFinite(waitingTime) ? Math.max(0, Math.trunc(waitingTime)) : NaN;
    if (!Number.isFinite(safeWaitingTime)) {
      setError("Bitte eine gültige Wartezeit eingeben.");
      return;
    }

    try {
      setError("");
      const response = await fetch(`${API_BASE}/food_court`, {
        method: "PUT",
        headers: authHeaders,
        credentials: "include",
        body: JSON.stringify({
          displayName: foodCourt.name,
          waitingTime: safeWaitingTime,
        }),
      });

      if (!response.ok) {
        const responseText = await response.text();
        throw new Error(responseText || "Wartezeit konnte nicht gespeichert werden.");
      }

      const updatedFoodCourt = (await response.json()) as FoodCourtResponse;
      setFoodCourt(updatedFoodCourt);
      setWaitingTime(updatedFoodCourt.waitingTime ?? safeWaitingTime);
    } catch (updateError) {
      setError(updateError instanceof Error ? updateError.message : "Unbekannter Fehler");
    }
  };

  const updateProductCountLocal = async (productId: string) => {
    try {
      setError("");
      const rawCount = editedCounts[productId] ?? 0;
      const newCount = Number.isFinite(rawCount) ? Math.max(0, Math.trunc(rawCount)) : 0;
      const data: Product = await updateProductCount(productId, newCount);
        await loadData();
    } catch (updateError) {
      setError(updateError instanceof Error ? updateError.message : "Unbekannter Fehler");
    }
  };

  const deleteProduct = async (productId: string) => {
    const shouldDelete = window.confirm("Produkt wirklich löschen?");
    if (!shouldDelete) {
      return;
    }

    try {
      setError("");
      await deleteProduct(productId);
      await loadData();
    } catch (deleteError) {
      setError(deleteError instanceof Error ? deleteError.message : "Unbekannter Fehler");
    }
  };

  return (
    <div className={styles.Page}>
      <div className={styles.Container}>
        <div className={styles.HeaderTop}>
          <div className={styles.StandTitle}>{foodCourt?.name ?? "Stand"}</div>
          <i className="fa-regular fa-pen-to-square" />
        </div>

        <div className={styles.HeaderRow}>
          <i className="fa-solid fa-bars" />
          <i className="fa-solid fa-user-chef" />
          <span className={styles.LoginLabel}>{loginLabel}</span>
          <div className={styles.ImageWrap}>
            {foodCourt?.id ? (
              <img src={`${API_BASE}/food_court/image/${foodCourt.id}`} alt="Food Court" className={styles.FoodImage} />
            ) : (
              <div className={styles.FoodImage} />
            )}
          </div>
        </div>

        <div className={styles.SectionHeader}>
          <button className={styles.BackBtn} onClick={() => navigate("/food_court")}>
            <i className="fa-solid fa-arrow-left" /> Produkte
          </button>
          <button className={styles.AddBtn}>
            <i className="fa-solid fa-plus" />
          </button>
        </div>

        {error && <div className={styles.Error}>{error}</div>}

        <div className={styles.ProductList}>
          {products.map((product) => (
            <div className={styles.ProductRow} key={product.id}>
              <div className={styles.ProductMain}>
                <div className={styles.ProductName}>{product.displayName}</div>
                <div className={styles.Warning}>Warnung bei Bestand</div>
              </div>

              <div className={styles.Price}>{product.price.toFixed(2)} €</div>

              <input
                className={styles.CountInput}
                type="number"
                min={0}
                value={editedCounts[product.id] ?? 0}
                onChange={(event) => {
                  const parsedValue = Number(event.target.value);
                  setEditedCounts((current) => ({
                    ...current,
                    [product.id]: Number.isFinite(parsedValue) ? parsedValue : 0,
                  }));
                }}
              />

              <button className={styles.IconBtn} onClick={() => updateProductCountLocal(product.id)}>
                <i className="fa-regular fa-pen-to-square" />
              </button>
              <button className={styles.IconBtn} onClick={() => deleteProduct(product.id)}>
                <i className="fa-regular fa-trash-can" />
              </button>
            </div>
          ))}
        </div>

        <div className={styles.WaitingSection}>
          <div>Geschätzte Wartezeit</div>
          <div className={styles.WaitingControls}>
            <input
              className={styles.WaitingInput}
              type="number"
              min={0}
              value={waitingTime}
              onChange={(event) => {
                const parsedValue = Number(event.target.value);
                setWaitingTime(Number.isFinite(parsedValue) ? parsedValue : 0);
              }}
            />
            <span>Min</span>
            <button className={styles.IconBtn} onClick={updateWaitingTime}>
              <i className="fa-regular fa-pen-to-square" />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default StandPage;
