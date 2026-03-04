import React, { useCallback, useEffect, useMemo, useState } from "react";
import styles from "./HomePage.module.css";
import { FoodCourt, Order, OrderStatus } from "../Types";

type FilterKey = "ALL" | OrderStatus;

const API_BASE = "http://10.45.128.255:8080";

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

function HomePage() {
	const token = localStorage.getItem("token");
	const [foodCourt, setFoodCourt] = useState<FoodCourt | null>(null);
	const [waitingTime, setWaitingTime] = useState<number>(15);
	const [orders, setOrders] = useState<Order[]>([]);
	const [statusSelection, setStatusSelection] = useState<Record<string, OrderStatus>>({});
	const [selectedOrderIds, setSelectedOrderIds] = useState<string[]>([]);
	const [activeFilter, setActiveFilter] = useState<FilterKey>("ALL");
	const [isLoading, setIsLoading] = useState<boolean>(false);
	const [error, setError] = useState<string>("");

	const authHeaders = useMemo(
		() => ({
			"Content-Type": "application/json",
			Authorization: `Bearer ${token ?? ""}`,
		}),
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

	const loadPageData = useCallback(async () => {
		if (!token) {
			setError("Kein Login-Token vorhanden. Bitte neu anmelden.");
			return;
		}

		setIsLoading(true);
		setError("");
		try {
			await Promise.all([fetchFoodCourt(), fetchOrders(activeFilter)]);
		} catch (fetchError) {
			setError(fetchError instanceof Error ? fetchError.message : "Unbekannter Fehler");
		} finally {
			setIsLoading(false);
		}
	}, [activeFilter, fetchFoodCourt, fetchOrders, token]);

	useEffect(() => {
		loadPageData();
	}, [loadPageData]);

	const countsByStatus = useMemo(() => {
		const initial: Record<OrderStatus, number> = {
			ORDERED: 0,
			IN_PROGRESS: 0,
			READY_FOR_PICKUP: 0,
			DONE: 0,
			CANCELED: 0,
		};

		return orders.reduce<Record<OrderStatus, number>>((accumulator, order) => {
			accumulator[order.status] += 1;
			return accumulator;
		}, initial);
	}, [orders]);

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
			await updateOrderStatus(orderId, selectedStatus);
			await fetchOrders(activeFilter);
		} catch (updateError) {
			setError(updateError instanceof Error ? updateError.message : "Unbekannter Fehler");
		}
	};

	const applyBulkStatusUpdate = async () => {
		if (selectedOrderIds.length === 0) {
			return;
		}

		try {
			await Promise.all(
				selectedOrderIds.map((orderId) =>
					updateOrderStatus(orderId, statusSelection[orderId] ?? "IN_PROGRESS"),
				),
			);
			await fetchOrders(activeFilter);
		} catch (updateError) {
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
					name: foodCourt.name,
					waitingTime,
				}),
			});

			if (!response.ok) {
				throw new Error("Wartezeit konnte nicht aktualisiert werden.");
			}

			const updatedFoodCourt = (await response.json()) as FoodCourt;
			setFoodCourt(updatedFoodCourt);
			setWaitingTime(updatedFoodCourt.waitingTime ?? waitingTime);
		} catch (updateError) {
			setError(updateError instanceof Error ? updateError.message : "Unbekannter Fehler");
		}
	};

	return (
		<div className={styles.Page}>
			<div className={styles.Container}>
				<div className={styles.TopHeader}>
					<div>
						<div className={styles.TopTitle}>{foodCourt?.name ?? "Food Court"}</div>
						<div>Live-Bestellübersicht</div>
					</div>
					<div className={styles.TopRight}>
						<div className={styles.Badge}>ID: {foodCourt?.id?.slice(0, 8) ?? "-"}</div>
						{foodCourt?.id ? (
							<img
								className={styles.FoodImage}
								src={`${API_BASE}/food_court/image/${foodCourt.id}`}
								alt="Food Court"
							/>
						) : (
							<div className={styles.FoodImage} />
						)}
					</div>
				</div>

				<div className={styles.InfoBar}>
					<div className={styles.InfoLeft}>
						<i className="fa-solid fa-list-check" />
						<span>Bestellungen</span>
						<span className={styles.Badge}>{orders.length}</span>
					</div>
					<div className={styles.InfoLeft}>
						<span>Abholbereit</span>
						<span className={styles.Badge}>{countsByStatus.READY_FOR_PICKUP}</span>
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
							onChange={(event) => setWaitingTime(Number(event.target.value))}
						/>
						<button className={styles.PrimaryButton} onClick={updateFoodCourtWaitingTime}>
							Speichern
						</button>
					</div>
					<button className={styles.SecondaryButton} onClick={loadPageData}>
						Aktualisieren
					</button>
				</div>

				<div className={styles.FilterRow}>
					{filterConfig.map((filter) => (
						<button
							key={filter.key}
							className={`${styles.FilterButton} ${activeFilter === filter.key ? styles.FilterActive : ""}`}
							onClick={() => setActiveFilter(filter.key)}
						>
							{filter.label}
						</button>
					))}
					<button
						className={styles.PrimaryButton}
						onClick={applyBulkStatusUpdate}
						disabled={selectedOrderIds.length === 0}
					>
						Alle updaten ({selectedOrderIds.length})
					</button>
				</div>

				{error && <div className={styles.Error}>{error}</div>}
				{isLoading && <div className={styles.Loading}>Lade Daten ...</div>}

				<div className={styles.OrderList}>
					{!isLoading && orders.length === 0 && (
						<div className={styles.Empty}>Keine Bestellungen für den gewählten Filter.</div>
					)}

					{orders.map((order) => {
						const groupedItems = order.orderItems.reduce<Record<string, number>>((accumulator, item) => {
							accumulator[item.displayName] = (accumulator[item.displayName] ?? 0) + item.count;
							return accumulator;
						}, {});

						const extras = order.orderItems
							.map((item) => item.extra)
							.filter((extraText) => extraText && extraText.trim().length > 0)
							.join(", ");

						return (
							<div className={styles.OrderCard} key={order.id}>
								<div>
									<div className={styles.OrderTop}>
										<div className={styles.OrderId}>Bestellung #{order.id.slice(0, 8)}</div>
										<div className={styles.OrderMeta}>
											<i className="fa-regular fa-clock" />
											<span>{order.waitingTime} min</span>
											<span className={styles.Badge}>{statusLabels[order.status]}</span>
										</div>
									</div>

									<div className={styles.Items}>
										{Object.entries(groupedItems).map(([itemName, count]) => (
											<div className={styles.ItemLine} key={itemName}>
												<span>{itemName}</span>
												<span>x {count}</span>
											</div>
										))}
									</div>

									<div className={styles.Extra}>Sonderwünsche: {extras || "-"}</div>
								</div>

								<div className={styles.CardActions}>
									<label>
										<input
											type="checkbox"
											checked={selectedOrderIds.includes(order.id)}
											onChange={() => toggleOrderSelection(order.id)}
										/>
										&nbsp;Auswahl
									</label>

									<select
										className={styles.StatusSelect}
										value={statusSelection[order.id] ?? order.status}
										onChange={(event) => {
											const value = event.target.value;
											if (!isOrderStatus(value)) {
												return;
											}
											setStatusSelection((currentSelection) => ({
												...currentSelection,
												[order.id]: value,
											}));
										}}
									>
										{Object.entries(statusLabels).map(([statusKey, label]) => (
											<option key={statusKey} value={statusKey}>
												{label}
											</option>
										))}
									</select>

									<button className={styles.SmallButton} onClick={() => applySingleStatus(order.id)}>
										Status setzen
									</button>
								</div>
							</div>
						);
					})}
				</div>
			</div>
		</div>
	);
}

export default HomePage;
