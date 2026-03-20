//Dummydaten

export type OrderStatus =
  | "ready_for_pickup"
  | "in_progress"
  | "done"
  | "canceled";

export interface Product {
  name: string;
  count: number;
  subItems?: Product[];
  extra: string;
}

export interface User {
  login_Nr: string;
  password: string;
  type: "GUEST" | "FOOD_COURT_WORKER" | "ADMIN";
}

export interface Ticket {
  id: string;
  login_Nr: string;
}

export interface Credits {
  login_Nr: string;
  credits: number;
}

export interface Order {
  loginNr: string;
  order_number: string;
  foodcourt: string;
  order_status: OrderStatus;
  waiting_time: number;
  orderItems: Product[];
}

export interface FoodCourtProductList{
  name: string;
  icon: string;
  count: number;
  sorts?: string[]
  subItems?:string[]
  type?: "Menue"
}

export interface FoodCourt {
  name: string;
  imageUrl: string;
  avg_waiting_time: number;
  products: FoodCourtProductList[]
}