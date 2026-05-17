const fs = require('fs');

const vueFile = 'src/views/uikit/FamilyPlanningAdmission.vue';
const serviceFile = 'src/service/FamilyPlanningService.js';

function fixFile(file) {
    if (!fs.existsSync(file)) return;
    let content = fs.readFileSync(file, 'utf8');
    
    // First apply specific path fixes
    content = content.replace(/\/api\/FamilyPlanning\/spouse/g, '/api/familyplanning/spouses');
    content = content.replace(/\/api\/FamilyPlanning\/physical-examination/g, '/api/familyplanning/physical-exam');
    content = content.replace(/\/api\/FamilyPlanning\/assessment-records/g, '/api/familyplanning/fpassessment');
    content = content.replace(/\/api\/FamilyPlanning\/pregnancy-checklist/g, '/api/familyplanning/pregnancychecklist');
    
    // Then fix all remaining /api/FamilyPlanning/ to /api/familyplanning/
    content = content.replace(/\/api\/FamilyPlanning\//g, '/api/familyplanning/');
    
    fs.writeFileSync(file, content);
    console.log(`Fixed endpoints in ${file}`);
}

fixFile(vueFile);
fixFile(serviceFile);
