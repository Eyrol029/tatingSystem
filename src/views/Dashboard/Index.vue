<script setup>
import { computed } from 'vue';
import { useUserDataStore, UserRole } from '@/stores/userData';
import AdminDashboard from './AdminDashboard.vue';
import MidwifeDashboard from './MidwifeDashboard.vue';

import PatientDashboard from './PatientDashboard.vue';

const useUser = useUserDataStore();

const loggedInUser = computed(() => {
    return useUser.user;
});
</script>
<template>
  <div>
    <AdminDashboard v-if="loggedInUser.role === UserRole.ADMIN"></AdminDashboard>
    <MidwifeDashboard v-if="loggedInUser.role === UserRole.MIDWIFE"></MidwifeDashboard>
    <PatientDashboard v-if="loggedInUser.role === UserRole.PATIENT"></PatientDashboard>
  </div>
</template>