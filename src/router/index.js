import { createRouter, createWebHistory } from 'vue-router';

import AppLayout from '@/layout/AppLayout.vue';
import NoNavLayout from '@/layout/NoNavLayout.vue';

import Dashboard from '@/views/Dashboard/Index.vue';
import Login from '@/views/uikit/Login.vue';

import { useUserDataStore } from '@/stores/userData';

const routes = [
    {
        path: '/',
        component: AppLayout,
        meta: { requiresAuth: true },
        children: [
            { path: '', name: 'dashboard', component: Dashboard },
            { path: 'uikit/PrenatalAdmission', component: () => import('@/views/uikit/PrenatalAdmission.vue') },
            { path: 'uikit/FamilyPlanningAdmission', component: () => import('@/views/uikit/FamilyPlanningAdmission.vue') },
            { path: 'uikit/UserAccountPatient', component: () => import('@/views/uikit/UserAccountPatient.vue') },
            { path: 'uikit/Patient', component: () => import('@/views/uikit/Patient.vue') },
            { path: 'uikit/Staff', component: () => import('@/views/uikit/Staff.vue') },
            { path: 'uikit/Wards', component: () => import('@/views/uikit/Wards.vue') },
            { path: 'uikit/PatientsMain', component: () => import('@/views/uikit/PatientsMain.vue') },
            { path: 'uikit/Service', component: () => import('@/views/uikit/Service.vue') },
            { path: 'uikit/PaymentDashboard', component: () => import('@/views/uikit/PaymentDashboard.vue') },
            { path: 'uikit/UserAccount', component: () => import('@/views/uikit/UserAccount.vue') },
            { path: 'uikit/PatientProfiling/:id', component: () => import('@/views/uikit/PatientProfiling.vue') },
            { path: 'uikit/PaymentDetails', component: () => import('@/views/uikit/PaymentDetails.vue') },
            { path: 'uikit/patientIndividual', component: () => import('@/views/uikit/PatientIndividual.vue') },
            { path: 'uikit/ReferralForm', component: () => import('@/views/uikit/ReferralForm.vue') },
            { path: 'uikit/Admission', component: () => import('@/views/uikit/Admission.vue') },
            { path: 'uikit/Logs', component: () => import('@/views/uikit/Logs.vue') },
            { path: 'uikit/ManageApp', component: () => import('@/views/uikit/ManageApp.vue') },
            { path: 'uikit/PatientCreateAppointment', component: () => import('@/views/uikit/PatientCreateAppointment.vue') },
            { path: 'uikit/PatientDashboard', component: () => import('@/views/Dashboard/PatientDashboard.vue') },
            { path: 'uikit/PatientService', component: () => import('@/views/uikit/PatientService.vue') },
            { path: 'uikit/MyServicesAndAppointment', component: () => import('@/views/uikit/MyServicesAndAppointment.vue') },
            { path: 'uikit/PaymentHistory', component: () => import('@/views/uikit/PaymentHistory.vue') },
            { path: 'uikit/viewListOfExpense', component: () => import('@/views/uikit/viewListOfExpense.vue') },
            { path: 'uikit/viewListOfRevenue', component: () => import('@/views/uikit/viewListOfRevenue.vue') },
            { path: 'uikit/viewListOfSOA', component: () => import('@/views/uikit/viewListOfSOA.vue') },
            { path: 'uikit/AdminCalendar', component: () => import('@/views/uikit/AdminCalendar.vue') },
            { path: 'uikit/MidwifeDashboard', component: () => import('@/views/Dashboard/MidwifeDashboard.vue') }, 
        ]
    },
    {
        path: '/',
        component: NoNavLayout,
        meta: { public: true },
        children: [
            { path: 'uikit/Login', name: 'Login', component: Login }
        ],
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

router.beforeEach((to, from, next) => {
    const userStore = useUserDataStore();

    // public pages
    if (to.matched.some((record) => record.meta.public)) {
        return next();
    }

    // protected pages
    if (to.matched.some((record) => record.meta.requiresAuth)) {
        if (!userStore.user) {
            return next({ path: '/uikit/Login' });
        }
    }

    next();
});

export default router;