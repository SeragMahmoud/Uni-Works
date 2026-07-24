# Repository Management Guide

## Naming Convention

Use numeric prefixes for ordered deliverables and `Title_Case_With_Underscores` for readability:

```text
01_Project_Charter.docx
02_Project_Scope.docx
03_WBS.pdf
04_Gantt_Chart.pdf
05_Risk_Register.xlsx
```

## Folder Rules

- Keep completed lifecycle documents in `docs/`.
- Keep editable Microsoft Project files in `microsoft-project/source-files/`.
- Keep exported visuals in `diagrams/` or `assets/`.
- Keep polished summary reports in `reports/`.
- Keep blank reusable forms in `templates/`.
- Keep obsolete versions in `archive/` instead of the main documentation flow.

## File Relocation And Renaming Plan

| Original File | New Location | Reason |
| --- | --- | --- |
| `feasibility-IISS.docx` | `docs/01_initiation/01_Feasibility_Study.docx` | Initiation artifact; renamed for consistent capitalization and ordering. |
| `IISS_Business_Case_v2.docx` | `docs/01_initiation/02_Business_Case.docx` | Business justification; version suffix removed because this is now the featured baseline. |
| `IISS_Project_Charter_v2.docx` | `docs/01_initiation/03_Project_Charter.docx` | Authorization document; version suffix removed. |
| `SWOT_Analysis_IISS.docx` | `docs/01_initiation/04_SWOT_Analysis.docx` | Strategic analysis supporting initiation and risk planning. |
| `IISS_Communication_Plan.docx` | `docs/02_planning/01_Communication_Plan.docx` | Communication management planning artifact. |
| `IISS_Training_Plan.docx` | `docs/02_planning/02_Training_Plan.docx` | Stakeholder readiness and adoption planning artifact. |
| `IISS_Individual_Goal_Template_v2.docx` | `docs/02_planning/03_Individual_Goals.docx` | Resource alignment artifact; renamed because it appears completed for the project. |
| `IISS_RAID_Log_v2.docx` | `docs/02_planning/04_RAID_Log.docx` | Risk, assumptions, issues, and dependencies planning/control artifact. |
| `IISS_Change_Request_v2.docx` | `docs/03_monitoring-and-control/01_Change_Request.docx` | Formal change control artifact. |
| `IISS_Closure_Report.docx` | `docs/04_closure/01_Project_Closure_Report.docx` | Project closure artifact. |
| `IISS Software Project Managment.mpp` | `microsoft-project/source-files/01_IISS_Project_Schedule.mpp` | Microsoft Project source file; spelling and spacing normalized. |
| `IISS_Project_Kickoff.pptx` | `presentations/01_Project_Kickoff.pptx` | Stakeholder kickoff presentation. |

## Recommended Git Commits

1. `chore: establish professional repository structure`
2. `docs: organize SPM lifecycle artifacts`
3. `docs: add Microsoft Project artifact documentation`
4. `docs: add repository navigation and management guide`
5. `chore: add gitignore and licensing notice`

## Remaining Improvements

- Standardize the official IISS acronym expansion across all Word and PowerPoint documents.
- Export the Microsoft Project schedule to PDF and place it in `microsoft-project/exports/`.
- Export Gantt, WBS, network, and milestone visuals into `diagrams/`.
- Add a final compiled SPM report to `reports/`.
- Review document metadata, template placeholders, and signatures before public publication.
