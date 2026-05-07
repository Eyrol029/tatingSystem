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
  { name: 'Admin', desc: 'System Administrator', value: UserRole.ADMIN },
  { name: 'Midwife', desc: 'Healthcare Midwife', value: UserRole.MIDWIFE },
  { name: 'Patient', desc: 'Patient User', value: UserRole.PATIENT },
]

const selectedRole = ref(UserRole.ADMIN)

const handleLogin = async () => {
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
  <div class="login-wrapper">
    <div class="left-panel">
      <h1 class="title">
        Tating’s <span>Birthing Home</span><br />
        and Family Planning Clinic
      </h1>
    </div>

    <div class="right-panel">
      <h2 class="welcome">Welcome!</h2>
      <p class="subtitle">Sign in to continue to your dashboard.</p>

      <div class="role-container">
        <div
          v-for="role in roles"
          :key="role.value"
          class="role-box"
          :class="{ active: selectedRole === role.value }"
          @click="selectedRole = role.value"
        >
          <h3>{{ role.name }}</h3>
          <p>{{ role.desc }}</p>
        </div>
      </div>

      <form class="login-form" @submit.prevent="handleLogin">
        <label>Email Address</label>
        <input
          type="email"
          v-model.trim="email"
          placeholder="Enter your email"
          required
        />

        <label>Password</label>
        <input
          type="password"
          v-model.trim="password"
          placeholder="Enter your password"
          required
        />

        <button class="login-btn" :disabled="userStore.isLoading">
          {{ userStore.isLoading ? 'Signing in...' : 'Sign In' }}
        </button>

        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </form>
    </div>
  </div>
</template>

<style scoped>
/* Layout */
.login-wrapper {
  display: flex;
  min-height: 100vh;
  background: #f8f8f8;
}

.left-panel {
  width: 40%;
  background: #8e4f70;
  padding: 50px;
  color: white;
}

.right-panel {
  width: 60%;
  padding: 50px;
}

/* Typography */
.title {
  font-size: 34px;
  font-weight: 700;
  line-height: 1.2;
}

.title span {
  color: #8db600;
}

.welcome {
  font-size: 34px;
  font-weight: 700;
}

.subtitle {
  margin-bottom: 20px;
  color: #555;
}

/* Role cards */
.role-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
  margin-bottom: 25px;
}

.role-box {
  padding: 15px;
  border-radius: 8px;
  border: 2px solid #ddd;
  background: white;
  cursor: pointer;
  transition: 0.2s ease;
}

.role-box:hover {
  border-color: #8e4f70;
}

.role-box.active {
  background: #8db600;
  border-color: #8db600;
}

/* Form */
.login-form {
  max-width: 360px;
  display: flex;
  flex-direction: column;
}

.login-form input {
  padding: 10px;
  margin-bottom: 12px;
  border-radius: 6px;
  border: 1px solid #bbb;
}

.login-btn {
  background: #8e4f70;
  color: white;
  padding: 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.login-btn:disabled {
  background: #aaa;
  cursor: not-allowed;
}

.error {
  color: red;
  margin-top: 10px;
}

/* Responsive */
@media (max-width: 1024px) {
  .login-wrapper {
    flex-direction: column;
  }

  .left-panel,
  .right-panel {
    width: 100%;
  }

  .role-container {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 600px) {
  .role-container {
    grid-template-columns: 1fr;
  }

  .title {
    font-size: 26px;
  }

  .welcome {
    font-size: 24px;
  }
}

/* Hover effect - applies to all role boxes */
.role-box:hover {
  border-color: #8e4f70;
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(142, 79, 112, 0.25);
  background: #f9f9f9;
}

.role-box.active:hover {
  background: #9ec700;
  box-shadow: 0 6px 16px rgba(141, 182, 0, 0.35);
}

/* Press/click effect */
.role-box:active,
.role-box.active:active {
  transform: scale(0.95) !important;
  transition: all 0.1s ease !important;
}
</style>
