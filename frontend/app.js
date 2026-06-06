const api = {
  onboarding: 'http://localhost:8081/onboarding-service',
  wallet: 'http://localhost:8083/wallet-service',
  txn: 'http://localhost:8084/txn-service',
};

let authToken = '';

const tokenSpan = document.getElementById('auth-token');
const registerResult = document.getElementById('register-result');
const otpResult = document.getElementById('otp-result');
const loginResult = document.getElementById('login-result');
const balanceResult = document.getElementById('balance-result');
const txnResult = document.getElementById('txn-result');
const historyResult = document.getElementById('history-result');

function updateTokenDisplay() {
  tokenSpan.textContent = authToken ? authToken : 'Not logged in';
}

function getHeaders() {
  const headers = { 'Content-Type': 'application/json' };
  if (authToken) {
    headers.Authorization = `Bearer ${authToken}`;
  }
  return headers;
}

async function request(url, options = {}) {
  const res = await fetch(url, options);
  const text = await res.text();
  if (!res.ok) {
    throw new Error(text || `${res.status} ${res.statusText}`);
  }
  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}

function showResult(element, value) {
  element.textContent = typeof value === 'string' ? value : JSON.stringify(value, null, 2);
}

const registerForm = document.getElementById('register-form');
registerForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  const data = Object.fromEntries(new FormData(registerForm));
  try {
    const response = await request(`${api.onboarding}/create/user`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(data),
    });
    showResult(registerResult, response);
  } catch (error) {
    showResult(registerResult, error.message);
  }
});

const otpForm = document.getElementById('otp-form');
otpForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  const data = Object.fromEntries(new FormData(otpForm));
  try {
    const response = await request(`${api.onboarding}/validate/otp`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(data),
    });
    showResult(otpResult, response);
  } catch (error) {
    showResult(otpResult, error.message);
  }
});

const loginForm = document.getElementById('login-form');
loginForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  const data = Object.fromEntries(new FormData(loginForm));
  try {
    const token = await request(`${api.onboarding}/user/login`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(data),
    });
    authToken = token;
    updateTokenDisplay();
    showResult(loginResult, { token });
  } catch (error) {
    showResult(loginResult, error.message);
  }
});

const balanceButton = document.getElementById('get-balance');
balanceButton.addEventListener('click', async () => {
  try {
    const response = await request(`${api.wallet}/get/balance`, {
      headers: getHeaders(),
    });
    showResult(balanceResult, response);
  } catch (error) {
    showResult(balanceResult, error.message);
  }
});

const txnForm = document.getElementById('transaction-form');
txnForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  const data = Object.fromEntries(new FormData(txnForm));
  data.amount = Number(data.amount);
  try {
    const response = await request(`${api.txn}/initiate/transaction`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(data),
    });
    showResult(txnResult, response);
  } catch (error) {
    showResult(txnResult, error.message);
  }
});

const historyButton = document.getElementById('get-history');
historyButton.addEventListener('click', async () => {
  try {
    const response = await request(`${api.txn}/get/transaction/history`, {
      headers: getHeaders(),
    });
    showResult(historyResult, response);
  } catch (error) {
    showResult(historyResult, error.message);
  }
});

updateTokenDisplay();
