import { initializeApp } from 'firebase/app';
import { getAuth, RecaptchaVerifier, signInWithPhoneNumber } from 'firebase/auth';

const firebaseConfig = {
  // ← EU vou colar aqui a config depois
};

const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);

export const sendOTP = async (phoneNumber) => {
  window.recaptchaVerifier = new RecaptchaVerifier(auth, 'recaptcha-container', {
    size: 'invisible'
  });
  return await signInWithPhoneNumber(auth, phoneNumber, window.recaptchaVerifier);
};
