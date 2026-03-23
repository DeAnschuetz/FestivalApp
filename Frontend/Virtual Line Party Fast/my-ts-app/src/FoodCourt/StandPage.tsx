import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./StandPage.module.css";
import { FoodCourtResponse } from "../Api/generated/ffbAPI.schemas";
import { apiDeleteProduct, getOwnFoodCourtProducts, updateProductCount } from "../Api/ffb/productApi";
import HeaderFoodCourt from "../Components/HeaderFoodCourt";
import  DeleteDialog from "../Components/DeleteDialog";
import { getOwnFoodCourt } from "../Api/ffb/foodCourtApi";
import { Product } from "../Api/ffb/types";


function StandPage() {
  const token = localStorage.getItem("token");
  const loginLabel = localStorage.getItem("loginNr") ?? "1234WP56-ZY09";
  const navigate = useNavigate();

  const [foodCourt, setFoodCourt] = useState<FoodCourtResponse | null>(null);
  const [products, setProducts] = useState<Product[]>([]);
  const [waitingTime, setWaitingTime] = useState<number>(15);
  const [editedCounts, setEditedCounts] = useState<Record<string, number>>({});
  const [error, setError] = useState<string>("");
  const [isCreateFormOpen, setIsCreateFormOpen] = useState(false);
  const [isCreatingProduct, setIsCreatingProduct] = useState(false);
  const [newDisplayName, setNewDisplayName] = useState("");
  const [newPrice, setNewPrice] = useState("");
  const [newSymbolIdentifier, setNewSymbolIdentifier] = useState("fa-solid fa-ban");
  const [newMinimalWarning, setNewMinimalWarning] = useState("");
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [productToDeleteId, setProductToDeleteId] = useState<string | null>(null);

   const loadData = useCallback(async () => {
    setError("");

    try {
      const [foodCourtResponse, productsResponse] = await Promise.all([
        await getOwnFoodCourt(),
        await getOwnFoodCourtProducts(),
      ]);

      setFoodCourt(foodCourtResponse);
      setWaitingTime(foodCourtResponse.waitingTime ?? 15);
      setProducts(productsResponse);
      setEditedCounts(
        productsResponse.reduce<Record<string, number>>((accumulator, product) => {
          accumulator[product.id] = product.productCount;
          return accumulator;
        }, {}),
      );
    } catch (loadError) {
      setError(loadError instanceof Error ? loadError.message : "Unbekannter Fehler");
    }
  }, []);

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
      const updatedFoodCourt = await getOwnFoodCourt();
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

  const openDeleteDialog = (productId: string) => {
    setProductToDeleteId(productId);
    setIsDeleteDialogOpen(true);
  };

  const handleCancelDelete = () => {
    setIsDeleteDialogOpen(false);
    setProductToDeleteId(null);
  };

  const handleConfirmDelete = async () => {
    if (!productToDeleteId) return;

    try {
      setError("");
      await apiDeleteProduct(productToDeleteId);
      setIsDeleteDialogOpen(false);
      setProductToDeleteId(null);
      await loadData();
    } catch (deleteError) {
      setError(deleteError instanceof Error ? deleteError.message : "Unbekannter Fehler");
    }
  }; 
  const createProduct = async () => {
    const displayName = newDisplayName.trim();
    if (!displayName) {
      setError("Produktname darf nicht leer sein.");
      return;
    }

    if (!newPrice.trim()) {
      setError("Preis ist ein Pflichtfeld.");
      return;
    }

    const price = Number(newPrice);
    if (!Number.isFinite(price) || price < 0) {
      setError("Preis muss eine gültige Zahl >= 0 sein.");
      return;
    }

    const symbolIdentifier = newSymbolIdentifier.trim();
    if (!symbolIdentifier) {
      setError("Symbol-Identifier darf nicht leer sein.");
      return;
    }

    if (!newMinimalWarning.trim()) {
      setError("Bestand Warnung ist ein Pflichtfeld.");
      return;
    }

    const minimalWarning = Number(newMinimalWarning);
    if (!Number.isInteger(minimalWarning) || minimalWarning < 0) {
      setError("Mindestwarnung muss eine ganze Zahl >= 0 sein.");
      return;
    }

    try {
      setIsCreatingProduct(true);
      setError("");
      setNewDisplayName("");
      setNewPrice("");
      setNewSymbolIdentifier("TEST");
      setNewMinimalWarning("");
      setIsCreateFormOpen(false);
      await loadData();
    } catch (createError) {
      setError(createError instanceof Error ? createError.message : "Unbekannter Fehler");
    } finally {
      setIsCreatingProduct(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  const handleOpenStand = () => {
    navigate("/food_court_view/stand");
  };

  return (
    <div className={styles.Page}>
      <div className={styles.Container}>
        <HeaderFoodCourt
          title={foodCourt?.name ?? "Stand"}
          loginLabel={loginLabel}
          foodCourtId={foodCourt?.id}
          token={token}
          onOpenStand={handleOpenStand}
          onLogout={handleLogout}
        />

        <div className={styles.SectionHeader}>
          <button className={styles.BackBtn} onClick={() => navigate("/food_court_view")}>
            <i className="fa-solid fa-arrow-left" /> Produkte
          </button>
          <button className={styles.AddBtn} onClick={() => setIsCreateFormOpen((open) => !open)}>
            <i className="fa-solid fa-plus" />
          </button>
        </div>

        {isCreateFormOpen && (
          <div className={styles.CreateForm}>
            <input
              className={styles.CreateInputWide}
              type="text"
              placeholder="Produktname"
              value={newDisplayName}
              onChange={(event) => setNewDisplayName(event.target.value)}
            />
            <input
              className={styles.CreateInput}
              type="number"
              placeholder="Preis in €"
              min={0}
              step="0.01"
              value={newPrice}
              onChange={(event) => setNewPrice(event.target.value)}
            />
            <input
              className={styles.CreateInput}
              type="number"
              min={0}
              step={1}
              placeholder="Bestand Warnung"
              value={newMinimalWarning}
              onChange={(event) => setNewMinimalWarning(event.target.value)}
            />
            <div className={styles.CreateActions}>
              <button
                className={styles.IconBtn}
                onClick={createProduct}
                disabled={isCreatingProduct}
              >
                {isCreatingProduct ? "..." : "OK"}
              </button>
              <button
                className={styles.IconBtn}
                onClick={() => setIsCreateFormOpen(false)}
                disabled={isCreatingProduct}
              >
                X
              </button>
            </div>
          </div>
        )}

        {error && <div className={styles.Error}>{error}</div>}

        <div className={styles.ProductList}>
          {products.map((product) => (
            <div className={styles.ProductRow} key={product.id}>
              <div className={styles.ProductMain}>
                <div className={styles.ProductName}>{product.displayName}</div>
                <div className={styles.Warning}>Warnung bei Bestand</div>
              </div>

              <div className={styles.Price}>{product.price.toFixed(2)} €</div>

              <div>
                <input
                  className={styles.CountInput}
                  type="number"
                  min={0}
                  value={editedCounts[product.id] ?? 0}
                  onChange={(event) => {
                    const parsedValue = Number(event.target.value);
                    setEditedCounts((current) => ({
                      ...current,
                      [product.id]: Number.isFinite(parsedValue) ? Math.max(0, parsedValue) : 0,
                    }));
                  }}
                  onKeyDown={(event) => {
                    if (event.key === "Enter") {
                      updateProductCount(product.id, editedCounts[product.id]);
                    }
                  }}
                />
              </div>

              <button className={styles.IconBtn} onClick={() => updateProductCountLocal(product.id)}>
                <i className="fa-regular fa-pen-to-square" />
              </button>
              <button className={styles.IconBtn} onClick={() => openDeleteDialog(product.id)}>
                <i className="fa-regular fa-trash-can" />
              </button>
            </div>
          ))}
        </div>

        <DeleteDialog open={isDeleteDialogOpen} onConfirm={handleConfirmDelete} onCancel={handleCancelDelete} />

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
