const fs = require('fs');

const vueFile = 'src/views/uikit/FamilyPlanningAdmission.vue';
const serviceFile = 'src/service/FamilyPlanningService.js';

function fixFile(file) {
    if (!fs.existsSync(file)) return;
    let content = fs.readFileSync(file, 'utf8');
    
    // Normalize to lowercase base path first to make replacements simpler
    content = content.replace(/api\/FamilyPlanning\//gi, 'api/familyplanning/');
    
    // Fix specific paths
    content = content.replace(/api\/familyplanning\/spouse['"]/g, "api/familyplanning/spouses'");
    content = content.replace(/api\/familyplanning\/physical-examination['"]/g, "api/familyplanning/physical-exam'");
    content = content.replace(/api\/familyplanning\/assessment-records['"]/g, "api/familyplanning/fpassessment'");
    content = content.replace(/api\/familyplanning\/pregnancy-checklist['"]/g, "api/familyplanning/pregnancychecklist'");
    
    fs.writeFileSync(file, content);
    console.log(`Fixed endpoints in ${file}`);
}

fixFile(vueFile);
fixFile(serviceFile);
