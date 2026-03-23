import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFirestore } from "firebase/firestore";

const firebaseConfig = {
  apiKey: "AIzaSyAzb-Ri84p6Z8GJJRmEB93NcsHbHbKVCks",
  authDomain: "priprideli.firebaseapp.com",
  projectId: "priprideli",
  storageBucket: "priprideli.firebasestorage.app",
  messagingSenderId: "618272619464",
  appId: "1:618272619464:web:9003837faed1973698defd",
};

console.log("API KEY USADA:", firebaseConfig.apiKey);

const app = initializeApp(firebaseConfig);

export const auth = getAuth(app);
export const db = getFirestore(app);

export default app;