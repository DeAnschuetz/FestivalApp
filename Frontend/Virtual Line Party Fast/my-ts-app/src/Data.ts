import { Credits, FoodCourt, Order, Ticket, User } from "./Types";

//images
import BurgerPalaceImg from "./Images/BurgerPalace.png";
import LetsRollImg from "./Images/LetsRoll.png";

const defaultUsers: User[] = [
  { login_Nr: "A-000-000-001", password: "admin1", type: "ADMIN" },
  {
    login_Nr: "F-000-000-001",
    password: "foodcourt1",
    type: "FOOD_COURT_WORKER",
  },
  { login_Nr: "V-000-000-001", password: "guest1", type: "GUEST" },
];

export const defaultTickets: Ticket[] = [
  { id: "1", login_Nr: "V-000-000-001" },
  { id: "1", login_Nr: "V-000-000-002" },
  { id: "1", login_Nr: "V-000-000-003" },
  { id: "1", login_Nr: "V-000-000-004" },
  { id: "1", login_Nr: "V-000-000-005" },
  { id: "1", login_Nr: "F-000-000-001" },
  { id: "1", login_Nr: "F-000-000-002" },
  { id: "1", login_Nr: "F-000-000-003" },
  { id: "1", login_Nr: "A-000-000-001" },
  { id: "1", login_Nr: "A-000-000-002" },
  { id: "1", login_Nr: "A-000-000-003" },
];

const defaultCredits: Credits[] = [{ login_Nr: "V-000-000-001", credits: 10 }];

const defaultOrders: Order[] = [
  {
    loginNr: "V-000-000-001",
    order_number: "#12345",
    foodcourt: "Burger Palace",
    order_status: "ready_for_pickup",
    waiting_time: 2,
    orderItems: [
      { name: "Cheeseburger", count: 2, subItems: [], extra: "" },
      { name: "Pommes", count: 1, subItems: [], extra: "" },
      { name: "Cola", count: 1, subItems: [], extra: "" },
    ],
  },
  {
    loginNr: "V-000-000-002",
    foodcourt: "Let's Roll",
    order_number: "#06789",
    order_status: "in_progress",
    waiting_time: 30,
    orderItems: [
      { name: "Noodels", count: 2, subItems: [], extra: "" },
      { name: "Happy Rolle Plate", count: 1, subItems: [], extra: "" },
    ],
  },
  {
    loginNr: "V-000-000-001",
    foodcourt: "Let's Roll",
    order_number: "#12237",
    waiting_time: 0,
    order_status: "done",
    orderItems: [
      { name: "Cumber Roll", count: 8, subItems: [], extra: "" },
      { name: "Deluex Roll Crispy", count: 4, subItems: [], extra: "" },
    ],
  },
  {
    loginNr: "V-000-000-001",
    foodcourt: "Burger Palace",
    order_number: "#12300",
    order_status: "canceled",
    waiting_time: 0,
    orderItems: [{ name: "Cheeseburger", count: 3, subItems: [], extra: "" }],
  },
  {
    loginNr: "V-000-000-001",
    order_number: "#08746",
    foodcourt: "Burger Palace",
    order_status: "in_progress",
    waiting_time: 5,
    orderItems: [
      { name: "Cheeseburger", count: 1, subItems: [], extra: "extra Käse" },
      { name: "Cheeseburger", count: 2, subItems: [], extra: "" },
    ],
  },
  {
    loginNr: "V-000-000-001",
    foodcourt: "Burger Palace",
    order_number: "#12120",
    order_status: "ready_for_pickup",
    waiting_time: 15,
    orderItems: [
      {
        name: "Menue 1",
        count: 1,
        subItems: [
          { name: "Cheeseburger", count: 1, extra: "" },
          { name: "Pommes Mittel", count: 1, extra: "ohne Salz" },
          { name: "Fanta", count: 1, extra: "" },
        ],
        extra: "extra Käse",
      },
    ],
  },
];

const defaultFoodCourts: FoodCourt[] = [
  {
    name: "Let's Roll",
    imageUrl: LetsRollImg,
    avg_waiting_time: 5,
    products: [
      {
        name: "Cumber Roll",
        icon: "fa fa-fish",
        count: 30,
        price: 1.50
      },
       {
        name: "Noodels",
        icon: "fa fa-bowl-rice",
        count: 15,
        price: 12.00
      },
       {
        name: "Deluex Roll Crispy",
        icon: "fa fa-drumstick-bite",
        count: 21,
        price: 4.50
      },
    ],
  },
  {
    name: "Burger Palace",
    imageUrl: BurgerPalaceImg,
    avg_waiting_time: 30,
    products: [
      {
        name: "Cheeseburger",
        icon: "fa fa-burger",
        count: 30,
        price: 8.00,
        type: 'Burger'
      },
       {
        name: "Pommes",
        icon: "fa fa-bacon",
        count: 15,
        price: 4.50
      },
       {
        name: "Drink",
        sorts: ["Cola", "Fanta"],
        icon: "fa fa-glass-water",
        count: 21,
        price: 2.50,
        type: 'Drink'
      },
      {  name: 'Menue 1',
        icon: "fa fa-utensils",
        count: 0,
        price: 12.00,
        type: 'Menue',
        subItems: [
          {name: "Burger nach Wahl", type: 'Burger'},
          {name: "Pommes Mittel"},
          {name: "Getränk 0,5l", type: 'Drink'},
          ],

      },
    ],
  },
];

// Helper: laden order defaults nehmen
function loadFromStorage<T>(key: string, defaultValue: T): T {
  const stored = localStorage.getItem(key);

  if (stored) {
    return JSON.parse(stored);
  }

  localStorage.setItem(key, JSON.stringify(defaultValue));
  return defaultValue;
}

// EXPORTS (wichtig!)
export let users: User[] = loadFromStorage("users", defaultUsers);
export let tickets: Ticket[] = loadFromStorage("tickets", defaultTickets);
export let creditsData: Credits[] = loadFromStorage("credits", defaultCredits);
export let orders: Order[] = loadFromStorage("orders", defaultOrders);
export let foodcourts: FoodCourt[] = loadFromStorage("foodcourts", defaultFoodCourts)

// Save Funktionen
export function saveUsers() {
  localStorage.setItem("users", JSON.stringify(users));
}

export function saveTickets() {
  localStorage.setItem("tickets", JSON.stringify(tickets));
}

export function saveCredits() {
  localStorage.setItem("credits", JSON.stringify(creditsData));
}

export function saveOrders() {
  localStorage.setItem("orders", JSON.stringify(orders));
}

export function saveFoodCourts() {
  localStorage.setItem("foodCourts", JSON.stringify(foodcourts));
}