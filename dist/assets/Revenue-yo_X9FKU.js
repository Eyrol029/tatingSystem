import{r as d,c as S,o as ee,f as l,g as s,h as t,w as x,v as b,t as i,F as T,l as A,j as y,p as te,n as ae,e as h,C as oe,z as ne}from"./index-Dh-KyBh7.js";const le={class:"p-6 bg-gray-100 min-h-screen"},se={class:"bg-white shadow-sm rounded-lg p-4 mb-4 flex flex-col md:flex-row md:items-end gap-3"},ie=["disabled"],re={class:"md:ml-auto text-sm font-semibold text-purple-900 bg-purple-50 px-3 py-2 rounded border border-purple-200"},de={class:"bg-white shadow rounded-lg overflow-hidden"},ue={class:"w-full text-sm"},ce={class:"p-3 font-mono text-xs text-gray-500 font-semibold"},pe={class:"p-3 font-medium"},me={class:"p-3 text-gray-600"},ve={class:"p-3 text-gray-600"},fe={class:"p-3"},ge={class:"p-3 font-semibold text-purple-950"},xe={class:"p-3"},be=["onClick"],ye={key:0,class:"text-center py-8 text-gray-400 text-sm"},he={key:1,class:"text-center py-8 text-gray-400 text-sm"},we={key:0,class:"fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50"},De={class:"bg-white w-full max-w-lg rounded-lg p-6 shadow-lg"},Se={class:"space-y-4"},ke={class:"relative mb-2"},_e=["value"],$e={key:0,class:"text-xs text-gray-400 mt-1"},Ne={key:1,class:"text-xs text-purple-500 mt-1"},Pe={key:2,class:"text-xs text-gray-400 mt-1"},Re={class:"text-sm font-medium"},Ce=["readonly"],R="http://localhost:8080/api/revenue",Ie="http://localhost:8080/api/billing/soa/patient",Te={__name:"Revenue",setup(Fe){const{confirmDelete:M}=ne(),k=d([]),_=d(!1),u=d(""),c=d("");function z(){u.value="",c.value=""}const v=S(()=>k.value.filter(a=>{const e=a.revenueDate?String(a.revenueDate).slice(0,10):"";return(!u.value||e>=u.value)&&(!c.value||e<=c.value)})),C=S(()=>v.value.reduce((a,e)=>a+Number(e.amount||0),0)),$=d([]),I=d([]),r=d(""),p=d(""),w=d(!1),U=S(()=>{const a={};return I.value.forEach(e=>{const o=e.patientID,m=String(e.caseNumber||"").trim();o!=null&&m&&m!=="---"&&!a[o]&&(a[o]=m)}),a});function N(a){return U.value[a]||"—"}const F=S(()=>{const a=p.value.trim().toLowerCase();return a?$.value.filter(e=>`${e.fName||""} ${e.lName||""}`.toLowerCase().includes(a)||String(e.patientID||"").includes(a)):$.value}),n=d({dealer:"",description:"",amount:"",patientID:null});function V(){const a=new Date,e=a.getFullYear(),o=String(a.getMonth()+1).padStart(2,"0"),m=String(a.getDate()).padStart(2,"0");return`${e}-${o}-${m}`}async function P(){var a;try{const e=await h.get(R);k.value=e.data}catch(e){console.error("Failed to load revenue records",e),alert("Failed to load revenue records: "+(((a=e.response)==null?void 0:a.data)||e.message))}}async function j(){try{const a=await h.get("http://localhost:8080/api/patients");$.value=a.data}catch(a){console.error("Failed to fetch patients list",a)}}async function H(){try{const a=await h.get("http://localhost:8080/api/patient-services");I.value=a.data}catch(a){console.error("Failed to fetch patient case numbers",a)}}async function B(){if(!r.value){n.value.patientID=null;return}w.value=!0;try{const e=(await h.get(`${Ie}/${r.value}`)).data;n.value.patientID=r.value,n.value.dealer=e.patientName||n.value.dealer,n.value.amount=e.amountPaid!=null?String(e.amountPaid):n.value.amount,n.value.description=n.value.description||`Total amount paid by ${e.patientName||"patient"} to date`}catch(a){console.error("Failed to fetch patient billing totals",a),alert("Could not load this patient's billing totals. You can still enter the amount manually.")}finally{w.value=!1}}function E(){_.value=!0}function L(){_.value=!1,O()}function O(){n.value={dealer:"",description:"",amount:"",patientID:null},r.value="",p.value=""}async function Y(){var a;if(!(!n.value.dealer||!n.value.amount))try{await h.post(R,{dealer:n.value.dealer,description:n.value.description,amount:Number(n.value.amount),revenueDate:V(),patientID:n.value.patientID}),await P(),L()}catch(e){console.error("Failed to save revenue",e),alert("Failed to save revenue: "+(((a=e.response)==null?void 0:a.data)||e.message))}}async function q(a){var e;if(await M())try{await h.delete(`${R}/${a}`),await P()}catch(o){console.error("Failed to delete revenue",o),alert("Failed to delete revenue: "+(((e=o.response)==null?void 0:e.data)||o.message))}}function f(a){return String(a??"").replace(/[&<>"']/g,e=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#039;"})[e])}function G(){if(!v.value.length){alert("No revenue records found for the selected filter.");return}const a=u.value||c.value?`${u.value||"Beginning"} to ${c.value||"Present"}`:"All dates",e=Number(C.value||0).toLocaleString("en-PH",{minimumFractionDigits:2}),o=new Date().toLocaleString("en-PH",{dateStyle:"medium",timeStyle:"short"}),m=v.value.map((g,W)=>{const J=`REV-${String(g.id).padStart(5,"0")}`,K=g.revenueDate?new Date(g.revenueDate).toLocaleDateString("en-PH",{year:"numeric",month:"short",day:"numeric"}):"—",X=N(g.patientID),Z=`₱${Number(g.amount||0).toLocaleString("en-PH",{minimumFractionDigits:2})}`;return`<tr>
      <td style="text-align: center;">${W+1}</td>
      <td style="font-family: monospace; font-weight: 600;">${f(J)}</td>
      <td>${f(K)}</td>
      <td><strong>${f(g.dealer||"—")}</strong></td>
      <td>${f(X)}</td>
      <td>${f(g.description||"—")}</td>
      <td style="text-align: right; font-weight: 600;">${f(Z)}</td>
    </tr>`}).join(""),D=window.open("","_blank","width=1000,height=850");if(!D){alert("Please allow pop-ups to print the report.");return}const Q=`<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Revenue Report - Tating Maternity Clinic</title>
  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
      color: #1f2937;
      margin: 28px;
      line-height: 1.4;
    }
    .header {
      border-bottom: 2px solid #6b21a8;
      padding-bottom: 14px;
      margin-bottom: 16px;
      display: flex;
      justify-content: space-between;
      align-items: flex-end;
    }
    .clinic-name {
      font-size: 22px;
      font-weight: bold;
      color: #6b21a8;
      margin: 0;
    }
    .report-title {
      font-size: 13px;
      color: #4b5563;
      margin-top: 4px;
      text-transform: uppercase;
      letter-spacing: 0.05em;
    }
    .meta {
      font-size: 12px;
      color: #6b7280;
      text-align: right;
    }
    .summary-bar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: #faf5ff;
      border: 1px solid #e9d5ff;
      border-radius: 6px;
      padding: 10px 16px;
      margin-bottom: 16px;
      font-size: 12px;
      color: #581c87;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      font-size: 12px;
    }
    th {
      background: #6b21a8;
      color: #ffffff;
      text-align: left;
      padding: 10px 8px;
      font-weight: 600;
    }
    td {
      padding: 8px;
      border-bottom: 1px solid #e5e7eb;
    }
    tr:nth-child(even) {
      background: #faf5ff;
    }
    .total-container {
      margin-top: 20px;
      display: flex;
      justify-content: flex-end;
    }
    .total-box {
      background: #f3e8ff;
      border: 1px solid #d8b4fe;
      border-radius: 6px;
      padding: 12px 24px;
      text-align: right;
      min-width: 220px;
    }
    .total-label {
      font-size: 11px;
      text-transform: uppercase;
      font-weight: bold;
      color: #6b21a8;
      letter-spacing: 0.05em;
    }
    .total-amount {
      font-size: 20px;
      font-weight: bold;
      color: #581c87;
      margin-top: 2px;
    }
    .footer {
      margin-top: 36px;
      border-top: 1px solid #e5e7eb;
      padding-top: 12px;
      font-size: 11px;
      color: #9ca3af;
      display: flex;
      justify-content: space-between;
    }
    @media print {
      body { margin: 12mm; }
      @page { size: auto; margin: 10mm; }
    }
  </style>
</head>
<body>
  <div class="header">
    <div>
      <div class="clinic-name">Tating Maternity Clinic</div>
      <div class="report-title">Revenue & Income Report</div>
    </div>
    <div class="meta">
      <div><strong>Date Generated:</strong> ${o}</div>
    </div>
  </div>

  <div class="summary-bar">
    <div><strong>Period:</strong> ${f(a)}</div>
    <div><strong>Total Transactions:</strong> ${v.value.length} record(s)</div>
  </div>

  <table>
    <thead>
      <tr>
        <th style="width: 35px; text-align: center;">#</th>
        <th>Revenue ID / Invoice</th>
        <th>Date Paid</th>
        <th>Paid By</th>
        <th>Patient Case No.</th>
        <th>Description</th>
        <th style="text-align: right;">Amount</th>
      </tr>
    </thead>
    <tbody>
      ${m}
    </tbody>
  </table>

  <div class="total-container">
    <div class="total-box">
      <div class="total-label">Total Revenue</div>
      <div class="total-amount">₱${e}</div>
    </div>
  </div>

  <div class="footer">
    <div>Generated from Tating Maternity Clinic Financial Management System</div>
    <div>Official Report</div>
  </div>

  <script>
    window.onload = () => {
      window.focus();
      window.print();
      window.onafterprint = () => window.close();
    };
  <\/script>
</body>
</html>`;D.document.open(),D.document.write(Q),D.document.close(),oe({name:"Revenue Report",type:"Revenue",period:a,details:`${v.value.length} record(s), total ₱${e}`})}return ee(()=>{P(),j(),H()}),(a,e)=>(l(),s("div",le,[t("div",{class:"flex flex-col sm:flex-row sm:justify-between sm:items-center gap-3 mb-6"},[e[8]||(e[8]=t("h2",{class:"text-xl font-semibold text-gray-800"},"Revenue Records",-1)),t("div",{class:"flex items-center gap-2"},[t("button",{onClick:G,class:"border border-purple-300 text-purple-700 bg-white hover:bg-purple-50 px-4 py-2 rounded-md text-sm font-medium shadow-sm transition"}," Print Report "),t("button",{onClick:E,class:"bg-purple-700 hover:bg-purple-800 text-white px-4 py-2 rounded-md text-sm font-medium shadow-sm transition"}," + Add Revenue ")])]),t("div",se,[t("div",null,[e[9]||(e[9]=t("label",{class:"block text-xs font-semibold text-gray-500 mb-1"},"From Date",-1)),x(t("input",{"onUpdate:modelValue":e[0]||(e[0]=o=>u.value=o),type:"date",class:"border rounded px-3 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-purple-500"},null,512),[[b,u.value]])]),t("div",null,[e[10]||(e[10]=t("label",{class:"block text-xs font-semibold text-gray-500 mb-1"},"To Date",-1)),x(t("input",{"onUpdate:modelValue":e[1]||(e[1]=o=>c.value=o),type:"date",class:"border rounded px-3 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-purple-500"},null,512),[[b,c.value]])]),t("button",{onClick:z,disabled:!u.value&&!c.value,class:"px-4 py-2 text-sm border rounded text-gray-600 hover:bg-gray-50 disabled:opacity-40"}," Clear Filter ",8,ie),t("div",re," Filtered Total: ₱"+i(C.value.toLocaleString("en-PH",{minimumFractionDigits:2})),1)]),t("div",de,[t("table",ue,[e[11]||(e[11]=t("thead",{class:"bg-gray-100"},[t("tr",null,[t("th",{class:"p-3 text-left"},"Revenue ID / Invoice No."),t("th",{class:"p-3 text-left"},"Paid by"),t("th",{class:"p-3 text-left"},"Patient Case No."),t("th",{class:"p-3 text-left"},"Date Paid"),t("th",{class:"p-3 text-left"},"Description"),t("th",{class:"p-3 text-left"},"Amount"),t("th",{class:"p-3 text-left"},"Action")])],-1)),t("tbody",null,[(l(!0),s(T,null,A(v.value,o=>(l(),s("tr",{key:o.id,class:"border-t"},[t("td",ce,"REV-"+i(String(o.id).padStart(5,"0")),1),t("td",pe,i(o.dealer),1),t("td",me,i(N(o.patientID)),1),t("td",ve,i(o.revenueDate?new Date(o.revenueDate).toLocaleDateString("en-US",{year:"numeric",month:"short",day:"numeric"}):"—"),1),t("td",fe,i(o.description),1),t("td",ge,"₱"+i(Number(o.amount||0).toLocaleString("en-PH",{minimumFractionDigits:2})),1),t("td",xe,[t("button",{onClick:m=>q(o.id),class:"text-red-600 hover:underline"}," Delete ",8,be)])]))),128))])]),k.value.length===0?(l(),s("div",ye," No revenue records yet. ")):v.value.length===0?(l(),s("div",he," No revenue records match the selected filters. ")):y("",!0)]),_.value?(l(),s("div",we,[t("div",De,[e[16]||(e[16]=t("h3",{class:"text-lg font-semibold mb-4"},"Add Revenue",-1)),t("div",Se,[t("div",null,[e[13]||(e[13]=t("label",{class:"text-sm font-medium"},"Link to Patient (optional)",-1)),t("div",ke,[x(t("input",{"onUpdate:modelValue":e[2]||(e[2]=o=>p.value=o),type:"text",placeholder:"Search patient by name or ID…",class:"w-full border rounded px-3 py-2 text-sm"},null,512),[[b,p.value]]),p.value?(l(),s("button",{key:0,type:"button",onClick:e[3]||(e[3]=o=>p.value=""),class:"absolute right-2 top-1/2 -translate-y-1/2 text-sm text-gray-400 hover:text-gray-600"},"×")):y("",!0)]),x(t("select",{"onUpdate:modelValue":e[4]||(e[4]=o=>r.value=o),onChange:B,class:"w-full border rounded px-3 py-2"},[e[12]||(e[12]=t("option",{value:""},"-- Manual entry (no patient) --",-1)),(l(!0),s(T,null,A(F.value,o=>(l(),s("option",{key:o.patientID,value:o.patientID},i(o.fName)+" "+i(o.lName)+" (Case: "+i(N(o.patientID))+") ",9,_e))),128))],544),[[te,r.value]]),p.value&&F.value.length===0?(l(),s("p",$e,' No patients match "'+i(p.value)+'". ',1)):y("",!0),w.value?(l(),s("p",Ne," Loading patient's total amount paid… ")):y("",!0),r.value&&!w.value?(l(),s("p",Pe," Amount is locked to this patient's total paid on their Statement of Account. The auto-generated Revenue ID also serves as the invoice/receipt number. ")):y("",!0)]),t("div",null,[e[14]||(e[14]=t("label",{class:"text-sm font-medium"},"Paid by",-1)),x(t("input",{"onUpdate:modelValue":e[5]||(e[5]=o=>n.value.dealer=o),class:"w-full border rounded px-3 py-2"},null,512),[[b,n.value.dealer]])]),t("div",null,[e[15]||(e[15]=t("label",{class:"text-sm font-medium"},"Description",-1)),x(t("textarea",{"onUpdate:modelValue":e[6]||(e[6]=o=>n.value.description=o),class:"w-full border rounded px-3 py-2"},null,512),[[b,n.value.description]])]),t("div",null,[t("label",Re," Amount (₱) "+i(r.value?"— auto-filled from billing":""),1),x(t("input",{type:"number","onUpdate:modelValue":e[7]||(e[7]=o=>n.value.amount=o),readonly:!!r.value,class:ae(["w-full border rounded px-3 py-2",r.value?"bg-gray-100 text-gray-600 cursor-not-allowed":""])},null,10,Ce),[[b,n.value.amount]])])]),t("div",{class:"flex justify-end gap-3 mt-6"},[t("button",{onClick:L,class:"px-6 py-2 bg-gray-200 rounded-md"}," Cancel "),t("button",{onClick:Y,class:"px-6 py-2 bg-purple-700 text-white rounded-md"}," Save ")])])])):y("",!0)]))}};export{Te as default};
