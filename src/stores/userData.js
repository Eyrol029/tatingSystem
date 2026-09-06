import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'

const BASE_URL = 'http://localhost:8080'

export const UserRole = {
    ADMIN: 'Admin',
    MIDWIFE: 'Midwife',
    PATIENT: 'Patient',
}

export const useUserDataStore = defineStore('userData', () => {
    const user = ref(null)
    const isLoading = ref(false)
    let heartbeatTimer = null

    function startHeartbeat() {
        stopHeartbeat()
        if (!user.value?.userID) return

        const sendHeartbeat = () => {
            axios.post(`${BASE_URL}/user/${user.value.userID}/heartbeat`).catch(() => {})
        }

        sendHeartbeat()
        heartbeatTimer = window.setInterval(sendHeartbeat, 60 * 1000)
    }

    function stopHeartbeat() {
        if (heartbeatTimer) {
            window.clearInterval(heartbeatTimer)
            heartbeatTimer = null
        }
    }

    // ✅ Check if user is logged in
    const isLoggedIn = computed(() => !!user.value)

    // ✅ Get current user role
    const userRole = computed(() => user.value?.role || null)

    // ✅ Role checkers
    const isAdmin = computed(() => user.value?.role === UserRole.ADMIN)
    const isMidwife = computed(() => user.value?.role === UserRole.MIDWIFE)
    const isPatient = computed(() => user.value?.role === UserRole.PATIENT)

    async function login(email, password, role) {
        isLoading.value = true
        try {
            const res = await axios.post(`${BASE_URL}/login`, {
                email,
                password,
                role
            })

            // Save user data
            user.value = res.data
            localStorage.setItem('user', JSON.stringify(res.data))
            startHeartbeat()

            return true
        } catch (e) {
            console.error('Login failed:', e.response?.data || e.message)
            return false
        } finally {
            isLoading.value = false
        }
    }

    function logout() {
        stopHeartbeat()
        user.value = null
        localStorage.removeItem('user')
    }

    // Restore user on page refresh
    function init() {
        const stored = localStorage.getItem('user')
        if (stored) {
            user.value = JSON.parse(stored)
            startHeartbeat()
        }
    }

    return {
        user,
        isLoading,
        isLoggedIn,  // ✅ new
        userRole,    // ✅ new
        isAdmin,     // ✅ new
        isMidwife,   // ✅ new
        isPatient,   // ✅ new
        login,
        logout,
        init
    }
})