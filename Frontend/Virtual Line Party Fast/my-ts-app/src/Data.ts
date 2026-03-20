import { Ticket, User } from "./Types";

export const users: User[] = [
  { login_Nr: "A-000-000-001", password: "admin1", type: "ADMIN" },
  {
    login_Nr: "F-000-000-001",
    password: "foodcourt1",
    type: "FOOD_COURT_WORKER",
  },
  { login_Nr: "V-000-000-001", password: "guest1", type: "GUEST" },
];

export const tickets: Ticket[] = [
  { id: "1", login_Nr: "V-000-000-002" },
  { id: "1", login_Nr: "V-000-000-003" },
  { id: "1", login_Nr: "V-000-000-004" },
  { id: "1", login_Nr: "V-000-000-005" },
];

