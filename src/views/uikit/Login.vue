<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { UserRole, useUserDataStore } from '@/stores/userData'

const userStore = useUserDataStore()
const router = useRouter()

const email = ref('')
const password = ref('')
const errorMessage = ref('')

const roles = [
  { name: 'Admin', desc: 'System administrator', value: UserRole.ADMIN },
  { name: 'Patient', desc: 'Patient user', value: UserRole.PATIENT },
]

const selectedRole = ref(UserRole.ADMIN)

const handleLogin = async () => {
  errorMessage.value = ''
  const success = await userStore.login(
    email.value,
    password.value,
    selectedRole.value
  )

  if (success) {
    router.push('/')
  } else {
    errorMessage.value = 'Invalid email or password.'
  }
}
</script>

<template>
  <div class="login-page">
    <div class="arch">
      <svg class="arch-shape" viewBox="0 0 1200 420" preserveAspectRatio="none" aria-hidden="true">
        <path d="M0,0 H1200 V300 C1000,300 900,420 600,420 C300,420 200,300 0,300 Z" />
      </svg>
      <div class="arch-content">
        <p class="eyebrow">Tating's Birthing Home &amp; Family Planning Clinic</p>
        <h1 class="title">Welcome back.<br />Let's get you signed in.</h1>
      </div>
    </div>

    <main class="form-area">
      <form class="login-card" @submit.prevent="handleLogin">
        <div class="role-tabs" role="tablist" aria-label="Sign in as">
          <button
            v-for="role in roles"
            :key="role.value"
            type="button"
            class="role-tab"
            :class="{ active: selectedRole === role.value }"
            role="tab"
            :aria-selected="selectedRole === role.value"
            @click="selectedRole = role.value"
          >
            <span class="role-name">{{ role.name }}</span>
            <span class="role-desc">{{ role.desc }}</span>
          </button>
        </div>

        <div class="field">
          <label for="email">Email address</label>
          <input
            id="email"
            type="email"
            v-model.trim="email"
            placeholder="you@example.com"
            required
          />
        </div>

        <div class="field">
          <label for="password">Password</label>
          <input
            id="password"
            type="password"
            v-model.trim="password"
            placeholder="Enter your password"
            required
          />
        </div>

        <button class="submit" type="submit" :disabled="userStore.isLoading">
          {{ userStore.isLoading ? 'Signing in…' : 'Sign in' }}
        </button>

        <p v-if="errorMessage" class="error" role="alert">{{ errorMessage }}</p>
      </form>
    </main>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Fraunces:opsz,wght@9..144,400;9..144,600;9..144,700&family=Work+Sans:wght@400;500;600&display=swap');

.login-page {
  min-height: 100vh;
  background: #6d2e52;
  color: #2a1e28;
  font-family: 'Work Sans', sans-serif;
  display: flex;
  flex-direction: column;
}

/* Arch banner */
.arch {
  position: relative;
  background: #f3ead9;
  color: #2a1e28;
  padding: 64px 24px 120px;
  overflow: hidden;
  border-bottom: 3px solid #8db600 ;
}

.arch-shape {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  fill: #8db600;
  fill-opacity: 0.14;
  z-index: 0;
  animation: settle 0.6s ease-out;
}

@keyframes settle {
  from {
    transform: translateY(-12px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.arch-content {
  position: relative;
  z-index: 1;
  max-width: 640px;
  margin: 0 auto;
  text-align: center;
}

.eyebrow {
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 0.02em;
  color: #5c7a00;
  margin: 0 0 18px;
}

.title {
  font-family: 'Fraunces', serif;
  font-weight: 600;
  font-size: clamp(28px, 4vw, 42px);
  line-height: 1.25;
  margin: 0;
  color: #2a1e28;
}

/* Form area */
.form-area {
  flex: 1;
  display: flex;
  justify-content: center;
  padding: 0 24px 64px;
  margin-top: -72px;
  position: relative;
  z-index: 2;
}

.login-card {
  width: 100%;
  max-width: 420px;
  background: #ffffff;
  border: 1px solid #ecdde5;
  padding: 32px;
  display: flex;
  flex-direction: column;
  gap: 22px;
}

/* Role tabs */
.role-tabs {
  display: flex;
  border-bottom: 1px solid #ecdde5;
  gap: 4px;
}

.role-tab {
  flex: 1;
  background: none;
  border: none;
  border-bottom: 2px solid transparent;
  padding: 8px 4px 14px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 3px;
  color: #8a7480;
  transition: color 0.15s ease, border-color 0.15s ease;
}

.role-tab:hover {
  color: #6d2e52;
}

.role-tab.active {
  color: #2a1e28;
  border-bottom-color: #8db600;
}

.role-name {
  font-weight: 600;
  font-size: 14.5px;
}

.role-desc {
  font-size: 12px;
  color: inherit;
  opacity: 0.75;
}

/* Fields */
.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.field label {
  font-size: 13px;
  font-weight: 500;
  color: #5c4d55;
}

.field input {
  padding: 11px 12px;
  border: 1px solid #ddd0d7;
  background: #fffdfc;
  font-family: inherit;
  font-size: 14.5px;
  color: #2a1e28;
  outline: none;
  transition: border-color 0.15s ease;
}

.field input:focus-visible {
  border-color: #6d2e52;
  box-shadow: 0 0 0 3px rgba(109, 46, 82, 0.12);
}

/* Submit */
.submit {
  margin-top: 4px;
  background: #6d2e52;
  color: #fff;
  border: none;
  padding: 13px;
  font-family: inherit;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.15s ease;
}

.submit:hover:not(:disabled) {
  background: #58253f;
}

.submit:disabled {
  background: #b9a6b1;
  cursor: not-allowed;
}

.error {
  margin: 0;
  color: #b3243c;
  font-size: 13.5px;
  text-align: center;
}

/* Responsive */
@media (max-width: 560px) {
  .arch {
    padding: 48px 20px 96px;
  }

  .role-tabs {
    flex-wrap: wrap;
  }

  .role-tab {
    flex: 1 1 30%;
  }
}

@media (prefers-reduced-motion: reduce) {
  .arch-shape {
    animation: none;
  }
}
</style>