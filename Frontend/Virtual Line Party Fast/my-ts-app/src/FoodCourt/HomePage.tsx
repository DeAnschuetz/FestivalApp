import { useCallback, useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

// css
import styles from "./HomePage.module.css";

//components
import HomePageFilterBar from "../Components/HomePageFilterBar";
import HomePageBulkActions from "../Components/HomePageBulkActions";
import HomePageOrderCard from "../Components/HomePageOrderCard";
import HeaderFoodCourt from "../Components/HeaderFoodCourt";

// api
import { FoodOrderStatus as OrderStatus } from "../Api/generated/ffbAPI.schemas"
import { FoodCourt, Order } from "../Api/ffb/types";
import { getOwnFoodCourt, getVisibleOrders, getVisibleOrdersByStatus, updateFoodOrderStatus } from "../Api/ffb";

type FilterKey = "ALL" | OrderStatus;

const statusLabels: Record<OrderStatus, string> = {
	ORDERED: "Bestellt",
	IN_PROGRESS: "In Arbeit",
	READY_FOR_PICKUP: "Abholbereit",
	DONE: "Abgeschlossen",
	CANCELED: "Storniert",
};

const filterConfig: { key: FilterKey; label: string }[] = [
	{ key: "ALL", label: "Alle" },
	...Object.entries(statusLabels).map(([key, label]) => ({
		key: key as OrderStatus,
		label,
	})),
];

const isOrderStatus = (value: unknown): value is OrderStatus =>
    typeof value === 'string' && value in statusLabels;

const getAllowedNextStatuses = (currentStatus: OrderStatus): OrderStatus[] => {
	switch (currentStatus) {
		case "ORDERED":
			return ["ORDERED", "IN_PROGRESS", "CANCELED"];
		case "IN_PROGRESS":
			return ["IN_PROGRESS", "READY_FOR_PICKUP", "CANCELED"];
		case "READY_FOR_PICKUP":
			return ["READY_FOR_PICKUP", "DONE"];
		case "DONE":
			return ["DONE"];
		case "CANCELED":
			return ["CANCELED"];
		default:
			return [];
	}
};

function HomePage() {
	const token = localStorage.getItem("token");
	const navigate = useNavigate();
	const [foodCourt, setFoodCourt] = useState<FoodCourt | null>(null);
	const [waitingTime, setWaitingTime] = useState<number>(15);
	const [orders, setOrders] = useState<Order[]>([]);
	const [allOrders, setAllOrders] = useState<Order[]>([]);
	const [statusSelection, setStatusSelection] = useState<Record<string, OrderStatus>>({});
	const [selectedOrderIds, setSelectedOrderIds] = useState<string[]>([]);
	const [activeFilter, setActiveFilter] = useState<FilterKey>("ALL");
	const [isLoading, setIsLoading] = useState<boolean>(false);
	const [error, setError] = useState<string>("");
	const [success, setSuccess] = useState<string>("");
	const loginLabel = localStorage.getItem("loginNr") ?? "1234WP56-ZY09";

	const handleLogout = () => {
		localStorage.removeItem("token");
		navigate("/login");
	};

	const handleOpenStand = () => {
		navigate("/food_court_view/stand");
	};

	const fetchFoodCourt = useCallback(async () => {
		const data: FoodCourt = await getOwnFoodCourt();

		setFoodCourt(data);
		setWaitingTime(data.waitingTime ?? 15);
        console.log("Food Court Daten geladen:", data);
	}, []);

	const fetchOrders = useCallback(
		async (filter: FilterKey) => {
			const data: Order[] =
				filter === "ALL"
					? await getVisibleOrders()
					: await getVisibleOrdersByStatus(filter);

            console.log("fetchOrders-Funktion erstellt mit Filter:", filter, "Ergebnis:", data);
			setOrders(data);
			
			// Immer alle Bestellungen laden für die Counters
			if (filter !== "ALL") {
				const allOrdersData = await getVisibleOrders();
				setAllOrders(allOrdersData);
			} else {
				setAllOrders(data);
			}
			
			setSelectedOrderIds([]);
			setStatusSelection(
				data.reduce<Record<string, OrderStatus>>((accumulator, order) => {
					accumulator[order.id] = order.status;
					return accumulator;
				}, {}),
			);
	}, []);

	const loadPageData = useCallback(async () => {
		setIsLoading(true);
		setError("");
		try {
			await Promise.all([fetchFoodCourt(), fetchOrders(activeFilter)]);
		} catch (fetchError) {
			setError(fetchError instanceof Error ? fetchError.message : "Unbekannter Fehler");
		} finally {
			setIsLoading(false);
		}
	}, [activeFilter, fetchFoodCourt, fetchOrders]);

	useEffect(() => {
		loadPageData();
	}, [loadPageData]);

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


	const countsByStatus = useMemo(() => {
		const initial: Record<OrderStatus, number> = {
			ORDERED: 0,
			IN_PROGRESS: 0,
			READY_FOR_PICKUP: 0,
			DONE: 0,
			CANCELED: 0,
		};
		console.log("AllOrders",allOrders);

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

	const bulkAllowedStatuses = useMemo(() => {
		if (selectedOrderIds.length === 0) {
			return Object.keys(statusLabels) as OrderStatus[];
		}

		const selectedOrders = orders.filter((o) => selectedOrderIds.includes(o.id));
		const perOrderAllowed = selectedOrders.map((o) => getAllowedNextStatuses(o.status));

		if (perOrderAllowed.length === 0) {
			return Object.keys(statusLabels) as OrderStatus[];
		}

		return perOrderAllowed[0].filter((status) =>
			perOrderAllowed.every((allowed) => allowed.includes(status)),
		);
	}, [selectedOrderIds, orders]);

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
		await updateFoodOrderStatus(orderId, newStatus);
	};

	const applySingleStatus = async (orderId: string) => {
		const selectedStatus = statusSelection[orderId];
		if (!selectedStatus) {
			return;
		}

		const order = orders.find((o) => o.id === orderId);
		if (!order) {
			return;
		}

		const allowed = getAllowedNextStatuses(order.status);
		if (!allowed.includes(selectedStatus)) {
			setError(`Status kann nicht von "${statusLabels[order.status]}" auf "${statusLabels[selectedStatus]}" geändert werden.`);
			return;
		}

		try {
			setSuccess("");
			await updateOrderStatus(orderId, selectedStatus);
			await fetchOrders(activeFilter);
		} catch (updateError) {
			setSuccess("");
			setError("Unbekannter Fehler");
		}
	};

	const applyBulkStatusUpdate = async (statusForUpdate: OrderStatus) => {
		if (selectedOrderIds.length === 0) {
			return;
		}

		const eligibleOrderIds = selectedOrderIds.filter((orderId) => {
			const order = orders.find((o) => o.id === orderId);
			return order && getAllowedNextStatuses(order.status).includes(statusForUpdate);
		});

		if (eligibleOrderIds.length === 0) {
			setError("Keine der ausgewählten Bestellungen kann auf diesen Status gesetzt werden.");
			return;
		}

		try {
			setError("");
			setSuccess("");
			const updatedCount = eligibleOrderIds.length;
			const skippedCount = selectedOrderIds.length - eligibleOrderIds.length;
			await Promise.all(
				eligibleOrderIds.map((orderId) => updateOrderStatus(orderId, statusForUpdate)),
			);
			await fetchOrders(activeFilter);
			setSelectedOrderIds([]);
			const msg = `Status für ${updatedCount} Bestellung${updatedCount === 1 ? "" : "en"} aktualisiert.`;
			setSuccess(skippedCount > 0 ? `${msg} ${skippedCount} übersprungen (ungültiger Übergang).` : msg);
		} catch (updateError) {
			setSuccess("");
			setError(updateError instanceof Error ? updateError.message : "Unbekannter Fehler");
		}
	};

	

	return (
		<div className={styles.Page}>
			<div className={styles.Container}>
				<HeaderFoodCourt
					title={foodCourt?.name ?? "Food Court"}
					loginLabel={loginLabel}
					foodCourtId={foodCourt?.id}
					token={token}
					onOpenStand={handleOpenStand}
					onLogout={handleLogout}
				/>
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
						<button className={styles.PrimaryButton}>
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

				{orders.length > 0 && (
					<HomePageBulkActions
						hasOrders={orders.length > 0}
						allVisibleOrdersSelected={allVisibleOrdersSelected}
						selectedCount={selectedOrderIds.length}
						statusLabels={statusLabels}
						allowedBulkStatuses={bulkAllowedStatuses}
						onToggleAll={toggleAllSelectedOrders}
						onApplyBulk={applyBulkStatusUpdate}
					/>
				)}

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
							statusLabels={statusLabels}						allowedStatuses={getAllowedNextStatuses(order.status)}							isOrderStatus={isOrderStatus}
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
