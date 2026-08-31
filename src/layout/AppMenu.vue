<script setup lang="ts">
import { computed } from 'vue';
import { useUserDataStore, UserRole } from '@/stores/userData';
import AppMenuItem from './AppMenuItem.vue';

// Access user store
const userStore = useUserDataStore();
const loggedInUser = computed(() => userStore.user);

// Menu model with roles
const model = [
    {
        label: 'Home',
        roles: [UserRole.ADMIN, UserRole.MIDWIFE, UserRole.PATIENT]
    },
    {
        items: [
            { label: 'Dashboard', icon: '', to: '/', roles: [UserRole.ADMIN] },
            { label: 'Staff', icon: '', to: '/uikit/Staff', roles: [UserRole.ADMIN, UserRole.MIDWIFE] },
            { label: 'Wards', icon: '', to: '/uikit/Wards', roles: [UserRole.ADMIN, UserRole.MIDWIFE] },
            { label: 'Service', icon: '', to: '/uikit/Service', roles: [UserRole.ADMIN, UserRole.MIDWIFE] },
            { label: 'Inventory', icon: '', to: '/uikit/Inventory', roles: [UserRole.ADMIN, UserRole.MIDWIFE] },
            { label: 'Expense', icon: '', to: '/uikit/Expense', roles: [UserRole.ADMIN] },
            {label: 'Revenue', icon: '', to: '/uikit/Revenue', roles: [UserRole.ADMIN] }, 
            {label: 'List of SOA', icon: '', to: '/uikit/viewListOfSOA', roles: [UserRole.ADMIN, UserRole.MIDWIFE] },
            { label: 'List of Patients', icon: '', to: '/uikit/PatientsMain', roles: [UserRole.ADMIN, UserRole.MIDWIFE] },
            { label: 'User Account', icon: '', to: '/uikit/UserAccount', roles: [UserRole.ADMIN] },
            { label: 'Logs', icon: '', to: '/uikit/Logs', roles: [UserRole.ADMIN, UserRole.MIDWIFE] },
            { label: 'Appointments', icon: '', to: '/uikit/AppointmentManagement', roles: [UserRole.ADMIN, UserRole.MIDWIFE] }, // new sa appointment 
            { label: 'Calendar', icon: '', to: '/uikit/AdminCalendar', roles: [UserRole.ADMIN] },
            { label: 'Patient Dashboard', icon: '', to: '/uikit/PatientDashboard', roles: [UserRole.PATIENT] },
            { label: 'Clinic Services', icon: '', to: '/uikit/PatientService', roles: [UserRole.PATIENT] }, 
            { label: 'My Medical Records', icon: '', to: '/uikit/PatientProfileview', roles: [UserRole.PATIENT] },
            { label: 'My Statement of Account', icon: '', to: '/uikit/MySOA', roles: [UserRole.PATIENT] },
      ]   
    }
];
// Filter model based on logged-in user's role
const filteredModel = computed(() => {
    if (!loggedInUser.value) return [];

    return model.map(section => {
        if (section.items) {
            const filteredItems = section.items.filter(item =>
                !item.roles || item.roles.includes(loggedInUser.value.role)
            )
            return { ...section, items: filteredItems }
        }
        if (!section.roles || section.roles.includes(loggedInUser.value.role)) {
            return section
        }
        return null;
    }).filter(Boolean);
});
</script>

<template>
  <ul class="layout-menu">
    <template v-for="(item, i) in filteredModel" :key="i">
      <app-menu-item :item="item" :index="i"></app-menu-item>
    </template>
  </ul>
</template>

<style lang="scss" scoped>
.layout-menu {
  list-style: none;
  margin: 0;
  padding: 0;
}

.menu-separator {
  height: 1px;
  background-color: #ccc;
  margin: 0.5rem 0;
}
</style>