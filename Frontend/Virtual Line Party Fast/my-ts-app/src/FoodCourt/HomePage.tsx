import React, { useCallback, useEffect, useMemo, useRef, useState } from "react";
import styles from "./HomePage.module.css";
import { FoodCourt, Order, OrderStatus } from "../Types";
import { useNavigate } from "react-router-dom";
import FoodCourtDropDown from "../Components/FoodCourtDropDown";
import HomePageFilterBar from "../Components/HomePageFilterBar";
import HomePageBulkActions from "../Components/HomePageBulkActions";
import HomePageOrderCard from "../Components/HomePageOrderCard";
type FilterKey = "ALL" | OrderStatus;

const API_BASE = "http://10.45.129.19:8080";

const filterConfig: { key: FilterKey; label: string }[] = [
	{ key: "ALL", label: "Alle" },
	{ key: "ORDERED", label: "Bestellt" },
	{ key: "IN_PROGRESS", label: "In Arbeit" },
	{ key: "READY_FOR_PICKUP", label: "Abholbereit" },
	{ key: "DONE", label: "Abgeschlossen" },
	{ key: "CANCELED", label: "Storniert" },
];

const statusLabels: Record<OrderStatus, string> = {
	ORDERED: "Bestellt",
	IN_PROGRESS: "In Arbeit",
	READY_FOR_PICKUP: "Abholbereit",
	DONE: "Abgeschlossen",
	CANCELED: "Storniert",
};

const isOrderStatus = (value: string): value is OrderStatus =>
	value === "ORDERED" ||
	value === "IN_PROGRESS" ||
	value === "READY_FOR_PICKUP" ||
	value === "DONE" ||
	value === "CANCELED";

const isUuid = (value: string) =>
	/^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$/.test(value);

function HomePage() {
	const token = localStorage.getItem("token");
	const navigate = useNavigate();
	const [foodCourt, setFoodCourt] = useState<FoodCourt | null>(null);
	const [waitingTime, setWaitingTime] = useState<number>(15);
	const [orders, setOrders] = useState<Order[]>([]);
	const [allOrders, setAllOrders] = useState<Order[]>([]);
	const [statusSelection, setStatusSelection] = useState<Record<string, OrderStatus>>({});
	const [bulkStatusSelection, setBulkStatusSelection] = useState<OrderStatus>("IN_PROGRESS");
	const [selectedOrderIds, setSelectedOrderIds] = useState<string[]>([]);
	const [activeFilter, setActiveFilter] = useState<FilterKey>("ALL");
	const [isLoading, setIsLoading] = useState<boolean>(false);
	const [foodCourtImageUrl, setFoodCourtImageUrl] = useState<string>("");
	const [useFallbackImage, setUseFallbackImage] = useState<boolean>(false);
	const [menuOpen, setMenuOpen] = useState<boolean>(false);
	const [error, setError] = useState<string>("");
	const [success, setSuccess] = useState<string>("");
	const menuRef = useRef<HTMLDivElement | null>(null);

	const directImageUrl =
		foodCourt?.id && isUuid(foodCourt.id) ? `${API_BASE}/food_court/image/${foodCourt.id}` : "";
	const loginLabel = localStorage.getItem("loginNr") ?? "1234WP56-ZY09";

	const handleLogout = () => {
		localStorage.removeItem("token");
		navigate("/login");
	};

	const handleOpenStand = () => {
		setMenuOpen(false);
		navigate("/food_court/stand");
	};

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

	const fetchFoodCourt = useCallback(async () => {
		const response = await fetch(`${API_BASE}/food_court`, {
			method: "GET",
			headers: authHeaders,
			credentials: "include",
		});

		if (!response.ok) {
			throw new Error("Food Court konnte nicht geladen werden.");
		}

		const result = (await response.json()) as FoodCourt;
		setFoodCourt(result);
		setWaitingTime(result.waitingTime ?? 15);
        console.log("Food Court Daten geladen:", result);
	}, [authHeaders]);

	const fetchOrders = useCallback(
		async (filter: FilterKey) => {
			const endpoint =
				filter === "ALL"
					? `${API_BASE}/food_order/list_all`
					: `${API_BASE}/food_order/list_all/by_status/${filter}`;

			const response = await fetch(endpoint, {
				method: "GET",
				headers: authHeaders,
				credentials: "include",
			});

			if (!response.ok) {
				throw new Error("Bestellungen konnten nicht geladen werden.");
			}

			const result = (await response.json()) as Order[];
            console.log("fetchOrders-Funktion erstellt mit Filter:", filter, "Ergebnis:", result);
			setOrders(result);
			setSelectedOrderIds([]);
			setStatusSelection(
				result.reduce<Record<string, OrderStatus>>((accumulator, order) => {
					accumulator[order.id] = order.status;
					return accumulator;
				}, {}),
			);
		},
       
		[authHeaders],
        
	);

	const fetchAllOrders = useCallback(async () => {
		const response = await fetch(`${API_BASE}/food_order/list_all`, {
			method: "GET",
			headers: authHeaders,
			credentials: "include",
		});

		if (!response.ok) {
			throw new Error("Bestellungszahlen konnten nicht geladen werden.");
		}

		const result = (await response.json()) as Order[];
		setAllOrders(result);
	}, [authHeaders]);

	const loadPageData = useCallback(async () => {
		setIsLoading(true);
		setError("");
		try {
			await Promise.all([fetchFoodCourt(), fetchOrders(activeFilter), fetchAllOrders()]);
		} catch (fetchError) {
			setError(fetchError instanceof Error ? fetchError.message : "Unbekannter Fehler");
		} finally {
			setIsLoading(false);
		}
	}, [activeFilter, fetchAllOrders, fetchFoodCourt, fetchOrders]);

	useEffect(() => {
		loadPageData();
	}, [loadPageData]);

	useEffect(() => {
		setUseFallbackImage(false);
		setFoodCourtImageUrl("");
	}, [foodCourt?.id]);

	useEffect(() => {
		const handleClickOutside = (event: MouseEvent) => {
			if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
				setMenuOpen(false);
			}
		};

		document.addEventListener("mousedown", handleClickOutside);
		return () => {
			document.removeEventListener("mousedown", handleClickOutside);
		};
	}, []);

	useEffect(() => {
		if (!success) {
			return;
		}

		const timeoutId = window.setTimeout(() => {
			setSuccess("");
		}, 2500);

		return () => {
			window.clearTimeout(timeoutId);
		};
	}, [success]);

	const loadFallbackImage = async () => {
		if (!foodCourt?.id || !isUuid(foodCourt.id)) {
			return;
		}

		const imageHeaders: Record<string, string> = {
			Accept: "image/png",
		};

		if (token) {
			imageHeaders.Authorization = `Bearer ${token}`;
		}

		try {
			const response = await fetch(`${API_BASE}/food_court/image/${foodCourt.id}`, {
				method: "GET",
				headers: imageHeaders,
				credentials: "include",
			});

			if (!response.ok) {
				return;
			}

			const blob = await response.blob();
			const objectUrl = URL.createObjectURL(blob);
			setFoodCourtImageUrl(objectUrl);
			setUseFallbackImage(true);
		} catch {
			setFoodCourtImageUrl("");
		}
	};

	const countsByStatus = useMemo(() => {
		const initial: Record<OrderStatus, number> = {
			ORDERED: 0,
			IN_PROGRESS: 0,
			READY_FOR_PICKUP: 0,
			DONE: 0,
			CANCELED: 0,
		};

		return allOrders.reduce<Record<OrderStatus, number>>((accumulator, order) => {
			accumulator[order.status] += 1;
			return accumulator;
		}, initial);
		}, [allOrders]);

		const getFilterCount = (filterKey: FilterKey) => {
			if (filterKey === "ALL") {
				return allOrders.length;
			}

			return countsByStatus[filterKey];
		};

	const allVisibleOrdersSelected =
		orders.length > 0 && orders.every((order) => selectedOrderIds.includes(order.id));

	const toggleAllSelectedOrders = () => {
		setSelectedOrderIds((currentSelection) => {
			const visibleOrderIds = orders.map((order) => order.id);
			const allSelected =
				visibleOrderIds.length > 0 && visibleOrderIds.every((orderId) => currentSelection.includes(orderId));

			if (allSelected) {
				return currentSelection.filter((orderId) => !visibleOrderIds.includes(orderId));
			}

			return Array.from(new Set([...currentSelection, ...visibleOrderIds]));
		});
	};

	const toggleOrderSelection = (orderId: string) => {
		setSelectedOrderIds((currentSelection) => {
			if (currentSelection.includes(orderId)) {
				return currentSelection.filter((id) => id !== orderId);
			}
			return [...currentSelection, orderId];
		});
	};

	const updateOrderStatus = async (orderId: string, newStatus: OrderStatus) => {
		setError("");
		const response = await fetch(`${API_BASE}/food_order/update/${orderId}/${newStatus}`, {
			method: "PUT",
			headers: authHeaders,
			credentials: "include",
		});

		if (!response.ok) {
			throw new Error("Status konnte nicht aktualisiert werden.");
		}
	};

	const applySingleStatus = async (orderId: string) => {
		const selectedStatus = statusSelection[orderId];
		if (!selectedStatus) {
			return;
		}

		try {
			setSuccess("");
			await updateOrderStatus(orderId, selectedStatus);
			await Promise.all([fetchOrders(activeFilter), fetchAllOrders()]);
		} catch (updateError) {
			setSuccess("");
			setError(updateError instanceof Error ? updateError.message : "Unbekannter Fehler");
		}
	};

	const applyBulkStatusUpdate = async () => {
		if (selectedOrderIds.length === 0) {
			return;
		}

		try {
			setError("");
			setSuccess("");
			const updatedCount = selectedOrderIds.length;
			await Promise.all(
				selectedOrderIds.map((orderId) => updateOrderStatus(orderId, bulkStatusSelection)),
			);
			await Promise.all([fetchOrders(activeFilter), fetchAllOrders()]);
			setSelectedOrderIds([]);
			setSuccess(
				`Status für ${updatedCount} Bestellung${updatedCount === 1 ? "" : "en"} aktualisiert.`,
			);
		} catch (updateError) {
			setSuccess("");
			setError(updateError instanceof Error ? updateError.message : "Unbekannter Fehler");
		}
	};

	const updateFoodCourtWaitingTime = async () => {
		if (!foodCourt) {
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
				}),
			});

			if (!response.ok) {
				const responseText = await response.text();
				throw new Error(responseText || "Wartezeit konnte nicht aktualisiert werden.");
			}

			const updatedFoodCourt = (await response.json()) as FoodCourt;
			setFoodCourt(updatedFoodCourt);
			setWaitingTime(updatedFoodCourt.waitingTime);
		} catch (updateError) {
			setError(updateError instanceof Error ? updateError.message : "Unbekannter Fehler");
		}
	};

	return (
		<div className={styles.Page}>
			<div className={styles.Container}>
				<div className={styles.TopHeader}>
					<div className={styles.TopLeft}>
						<div className={styles.MenuAnchor} ref={menuRef}>
							<button className={styles.MenuButton} onClick={() => setMenuOpen((open) => !open)}>
								<i className="fa fa-bars" />
							</button>
							<div className={styles.LoginTag}>{loginLabel}</div>
							{menuOpen && (
								<FoodCourtDropDown onOpenStand={handleOpenStand} onLogout={handleLogout} />
							)}
						</div>
						<div className={styles.TopTitle}>{foodCourt?.name ?? "Food Court"}</div>
					</div>
					<div className={styles.TopRight}>
						{/* <div className={styles.Badge}>UUID: {foodCourt?.id ?? "-"}</div> */}
						{(directImageUrl || foodCourtImageUrl) ? (
							<img
								className={styles.FoodImage}
								src={useFallbackImage ? foodCourtImageUrl : directImageUrl}
								alt="Food Court"
								onError={() => {
									if (!useFallbackImage) {
										loadFallbackImage();
									}
								}}
							/>
						) : (
							<div className={styles.FoodImage} />
						)}
					</div>
				</div>
				<div className={styles.ControlsRow}>
					<div className={styles.WaitingGroup}>
						<i className="fa-regular fa-clock" />
						<span>Wartezeit (Minuten)</span>
						<input
							className={styles.NumberInput}
							type="number"
							min={0}
							value={waitingTime}
							onChange={(event) => {
								const parsedValue = Number(event.target.value);
								setWaitingTime(Number.isFinite(parsedValue) ? parsedValue : 0);
							}}
						/>
						<button className={styles.PrimaryButton} onClick={updateFoodCourtWaitingTime}>
							Speichern
						</button>
					</div>
					<button className={styles.SecondaryButton} onClick={loadPageData}>
						Aktualisieren
					</button>
				</div>

				<HomePageFilterBar
					filters={filterConfig}
					activeFilter={activeFilter}
					onSelectFilter={(filterKey) => setActiveFilter(filterKey as FilterKey)}
					getFilterCount={(filterKey) => getFilterCount(filterKey as FilterKey)}
				/>

				<HomePageBulkActions
					hasOrders={orders.length > 0}
					allVisibleOrdersSelected={allVisibleOrdersSelected}
					bulkStatusSelection={bulkStatusSelection}
					selectedCount={selectedOrderIds.length}
					statusLabels={statusLabels}
					onToggleAll={toggleAllSelectedOrders}
					onBulkStatusChange={setBulkStatusSelection}
					onApplyBulk={applyBulkStatusUpdate}
				/>

				{error && <div className={styles.Error}>{error}</div>}
				{success && <div className={styles.Success}>{success}</div>}
				{isLoading && <div className={styles.Loading}>Lade Daten ...</div>}

				<div className={styles.OrderList}>
					{!isLoading && orders.length === 0 && (
						<div className={styles.Empty}>Keine Bestellungen für den gewählten Filter.</div>
					)}

					{orders.map((order) => (
						<HomePageOrderCard
							key={order.id}
							order={order}
							isSelected={selectedOrderIds.includes(order.id)}
							selectedStatus={statusSelection[order.id] ?? order.status}
							statusLabels={statusLabels}
							isOrderStatus={isOrderStatus}
							onToggleSelected={() => toggleOrderSelection(order.id)}
							onStatusChange={(value) =>
								setStatusSelection((currentSelection) => ({
									...currentSelection,
									[order.id]: value,
								}))
							}
							onApplyStatus={() => applySingleStatus(order.id)}
						/>
					))}
				</div>
			</div>
		</div>
	);
}

export default HomePage;
