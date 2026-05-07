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
            { label: 'Dashboard', icon: 'pi pi-fw pi-home', to: '/', roles: [UserRole.ADMIN] },
            { label: 'Staff', icon: 'pi pi-fw pi-user', to: '/uikit/Staff', roles: [UserRole.ADMIN, UserRole.MIDWIFE] },
            { label: 'Wards', icon: 'pi pi-fw pi-warehouse', to: '/uikit/Wards', roles: [UserRole.ADMIN, UserRole.MIDWIFE] },
            { label: 'Service', icon: 'pi pi-fw pi-book', to: '/uikit/Service', roles: [UserRole.ADMIN, UserRole.MIDWIFE] },
            { label: 'Payment Dashboard', icon: 'pi pi-fw pi-money-bill', to: '/uikit/PaymentDashboard', roles: [UserRole.ADMIN] },
            { label: 'List of Patients', icon: 'pi pi-fw pi-chart-line', to: '/uikit/PatientsMain', roles: [UserRole.ADMIN, UserRole.MIDWIFE] },
            { label: 'User Account', icon: 'pi pi-fw pi-users', to: '/uikit/UserAccount', roles: [UserRole.ADMIN] },
            { label: 'Logs', icon: 'pi pi-fw pi-home', to: '/uikit/Logs', roles: [UserRole.ADMIN, UserRole.MIDWIFE] },
            { label: 'Appointments', icon: 'pi pi-fw pi-calendar', to: '/uikit/ManageApp', roles: [UserRole.ADMIN, UserRole.MIDWIFE] },
            { label: 'Calendar', icon: 'pi pi-fw pi-calendar', to: '/uikit/AdminCalendar', roles: [UserRole.ADMIN] },
            { label: 'Patient Dashboard', icon: 'pi pi-fw pi-user', to: '/uikit/PatientDashboard', roles: [UserRole.PATIENT] },
            { label: 'Create Appointment', icon: 'pi pi-fw pi-calendar-plus', to: '/uikit/PatientCreateAppointment', roles: [UserRole.PATIENT] },
            { label: 'Clinic Services', icon: 'pi pi-fw pi-book', to: '/uikit/PatientService', roles: [UserRole.PATIENT] },
            { label: 'My Services and Appointments', icon: 'pi pi-fw pi-list', to: '/uikit/MyServicesAndAppointment', roles: [UserRole.PATIENT] },
            { label: 'Payment History', icon: 'pi pi-fw pi-money-bill', to: '/uikit/PaymentHistory', roles: [UserRole.PATIENT] },
           
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
