# Management Architecture

## Lifecycle Model

```mermaid
flowchart TD
    Start[Project concept] --> Feasibility[Feasibility study]
    Feasibility --> BusinessCase[Business case]
    BusinessCase --> Charter[Project charter]
    Charter --> Planning[Planning package]
    Planning --> RAID[RAID log]
    Planning --> Communication[Communication plan]
    Planning --> Training[Training plan]
    Planning --> Goals[Individual goals]
    RAID --> Change[Change request control]
    Change --> Closure[Closure report]
    Closure --> End[Project handover]
```

## Knowledge-Area Mapping

| Knowledge area | Repository evidence |
| --- | --- |
| Integration | Charter, change request, closure report. |
| Scope | Feasibility, business case, charter. |
| Schedule | Microsoft Project structure referenced by target repository, future Gantt/WBS exports. |
| Cost | Business case assumptions. |
| Quality | Training plan and closure acceptance context. |
| Resource | Individual goals and planning artifacts. |
| Communication | Communication plan and kickoff presentation. |
| Risk | RAID log and SWOT analysis. |
| Stakeholder | Communication plan, training plan, kickoff material, closure report. |

## Governance Flow

```mermaid
flowchart LR
    Request[Change or risk identified] --> Assess[Assess impact]
    Assess --> Decision{Approve?}
    Decision -- Yes --> Update[Update plan and communicate]
    Decision -- No --> Record[Record decision]
    Update --> Monitor[Monitor outcome]
    Record --> Monitor
```
