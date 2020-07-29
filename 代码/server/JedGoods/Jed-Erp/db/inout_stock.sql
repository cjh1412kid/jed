---进出库汇总查询---
select  sum(Journal.InCome_qty) as income_qty,sum(Journal.OutCome_qty) as outcome_qty,sum(journal.income_amount) as
income_amount,sum(journal.outcome_amount) as outcome_amount
,Journal.Inventory_id,inventory.description,inventory.unit,Journal.contactNum,mis_customer.ContactName_L  from DRP_Inventory_Journal  Journal Left
Join Inventory On Journal.Inventory_ID=Inventory.Inventory_ID left join mis_customer on Journal.contactNum=mis_customer.contactNum Left Join
vDRP_bas_Stock on vDRP_bas_stock.stock_id=Journal.Stock_id left join bas_Model on Inventory.model=bas_Model.Model_id  where 1=1  and  Journal.Date <=
'2019-03-29' And Journal.Date >= '2019-03-28' And Inventory.Type=1 And vDRP_bas_Stock.contactcode like 'cnt036%'Group by
Journal.Inventory_id,Journal.contactNum,mis_customer.ContactName_L,inventory.unit,inventory.description

---进出库明细查询---
select   Journal.date,Journal.Evidence_Number,
DRP_sys_BillInfo.BillName,Vdrp_bas_Stock.Description as Stock,
Journal.Inventory_id,Inventory.Description,inventory.Size_type,
journal.price,vPrice.PlanWholesale,
(isNull(vprice.plancost,0)-isNull(journal.price,0))*Journal.InCome_qty as InBanlance,
Journal.InCome_qty,Journal.OutCome_qty,vprice.plancost,income_amount,
outcome_amount,Unit,Journal.contactnum,mis_Customer.contactName_L,
Journal.Size1,Journal.Size2,Journal.Size3,Journal.Size4,Journal.Size5,Journal.Size6,Journal.Size7,Journal.Size8,Journal.Size9,Journal.Size10,
Journal.Size11,Journal.Size12,Journal.Size13,Journal.Size14,Journal.Size15,Journal.Size16,Journal.Size17,Journal.Size18,
isNull(vprice.plancost,0)*(income_qty+outcome_qty) as cost_amount,
(isNull(vprice.PlanWholesale,0)-isNull(journal.price,0))*Journal.OutCome_qty as OutBanlance,
income_amount+outcome_amount-isNull(vprice.plancost,0)*(income_qty+outcome_qty) as offset_amount,
Journal.summary , Journal.poster,EvidAtTimePrice.AtTimePrice
from DRP_Inventory_Journal  Journal
Left  Join Inventory On Journal.Inventory_ID=Inventory.Inventory_ID
Left Join Vdrp_bas_Stock on Vdrp_bas_stock.stock_id=Journal.Stock_id
Left Join bas_Model On bas_Model.Model_id=Inventory.Model
Left Join DRP_sys_BillInfo  DRP_sys_BillInfo on DRP_sys_BillInfo.billType=Journal.type  and Journal.company_id=DRP_sys_BillInfo.contactNum
left join mis_Customer on Journal.ContactNum=mis_Customer.contactNum
left Join DRP_bas_Price  vPrice on vPrice.company_id=Journal.company_id
and Journal.Inventory_id=vPrice.Inventory_ID
Left Join DRP_stk_EvidenceDetail  EvidAtTimePrice  On Journal.Evidence_Number=EvidAtTimePrice.Evidence_Number
And Journal.SubID=EvidAtTimePrice.SubID
where 1=1  and  Journal.Date <= '2019-03-29' And Journal.Date >= '2019-03-28' And Inventory.Type=1 And vDRP_bas_Stock.contactcode like 'cnt036%'



---库存查询---
--(stock_id='CLA' or stock_id='HCA' or stock_id='HDC' or stock_id='HDZ' or stock_id='HZA' or stock_id='wzcxc' or stock_id='ZJ' or stock_id='ZJCL' or stock_id='zjhg' or stock_id='ZPA')--
select  case when A.Inventory_ID is Null then B.Inventory_ID else A.Inventory_ID end as
Inventory_ID,Inventory.Description,Inventory.size_type,Inventory.Unit,isNull(A.Qty,0)+isNull(B.Qty,0) as balance_qty,  isNull(A.size1,0)+isNull(B.Size1,0) as Size1,
isNull(A.size2,0)+isNull(B.Size2,0)  as Size2,isNull(A.size3,0)+isNull(B.Size3,0) as Size3,isNull(A.size4,0)+isNull(B.Size4,0)  as Size4,isNull(A.size5,0)+isNull(B.Size5,0) as
Size5,isNull(A.size6,0)+isNull(B.Size6,0) as Size6,isNull(A.size7,0)+isNull(B.Size7,0) as Size7,isNull(A.size8,0)+isNull(B.Size8,0) as Size8,isNull(A.size9,0)+isNull(B.Size9,0)
as Size9,isNull(A.size10,0)+isNull(B.Size10,0)  as Size10,isNull(A.size11,0)+isNull(B.Size11,0) as Size11, isNull(A.size12,0)+isNull(B.Size12,0) as
Size12,isNull(A.size13,0)+isNull(B.Size13,0) as Size13, isNull(A.size14,0)+isNull(B.Size14,0) as Size14,isNull(A.size15,0)+isNull(B.Size15,0) as Size15,
isNull(A.size16,0)+isNull(B.Size16,0) as Size16,isNull(A.size17,0)+isNull(B.Size17,0) as Size17, isNull(A.size18,0)+isNull(B.Size18,0) as
Size18,isNull(A.size19,0)+isNull(B.Size19,0) as Size19, isNull(A.size20,0)+isNull(B.Size20,0) as Size20,isNull(A.size21,0)+isNull(B.Size21,0) as Size21,
isNull(A.size22,0)+isNull(B.Size22,0) as Size22,isNull(A.size23,0)+isNull(B.Size23,0) as Size23, isNull(A.size24,0)+isNull(B.Size24,0) as
Size24,isNull(A.size25,0)+isNull(B.Size25,0) as Size25 From     (select begin1.Inventory_ID,sum(Qty) as Qty,Sum(Size1) as Size1,Sum(Size2) as
Size2,Sum(Size3) as Size3,Sum(Size4) as Size4,Sum(Size5) as Size5,Sum(Size6) as Size6,Sum(Size7) as Size7,Sum(Size8) as Size8,Sum(Size9) as Size9,Sum(Size10) as
Size10,Sum(Size11) as Size11,Sum(Size12) as Size12,Sum(Size13) as Size13,Sum(Size14) as Size14,Sum(Size15) as Size15,Sum(Size16) as Size16,Sum(Size17) as Size17,Sum(Size18) as
Size18,Sum(Size19) as Size19,Sum(Size20) as Size20,Sum(Size21) as Size21,Sum(Size22) as Size22,Sum(Size23) as Size23,Sum(Size24) as Size24,Sum(Size25) as Size25 From
DRP_Inventory_Begin  begin1 Left Join Inventory On Inventory.Inventory_ID=begin1.Inventory_ID   Left Join bas_Model On bas_Model.Model_id=Inventory.Model  Left Join
vDRP_bas_Stock on vDRP_bas_stock.stock_id=begin1.stock_id WHERE Inventory.Type=1 And (vDRP_bas_stock.stock_id='ZPA') AND      begin1.year=2019 and PeriodNumber=2 and ContactCode
like 'cnt036%'  Group by begin1.Inventory_ID) as A  Full Join     (select Journal.Inventory_ID, sum(income_qty-outcome_Qty) as Qty,sum(case  when income_qty<>0 then size1 else
-size1 end) as size1,sum(case  when income_qty<>0 then size2 else -size2 end) as size2,sum(case  when income_qty<>0 then size3 else -size3 end) as size3,sum(case  when
income_qty<>0 then size4 else -size4 end) as size4,sum(case  when income_qty<>0 then size5 else -size5 end) as size5,sum(case  when income_qty<>0 then size6 else -size6 end) as
size6,sum(case  when income_qty<>0 then size7 else -size7 end) as size7,sum(case  when income_qty<>0 then size8 else -size8 end) as size8,sum(case  when income_qty<>0 then size9
else -size9 end) as size9,sum(case  when income_qty<>0 then size10 else -size10 end) as size10,sum(case  when income_qty<>0 then size11 else -size11 end) as size11,sum(case
when income_qty<>0 then size12 else -size12 end) as size12,sum(case  when income_qty<>0 then size13 else -size13 end) as size13,sum(case  when income_qty<>0 then size14 else
-size14 end) as size14,sum(case  when income_qty<>0 then size15 else -size15 end) as size15,sum(case  when income_qty<>0 then size16 else -size16 end) as size16,sum(case  when
income_qty<>0 then size17 else -size17 end) as size17,sum(case  when income_qty<>0 then size18 else -size18 end) as size18,sum(case  when income_qty<>0 then size19 else -size19
end) as size19,sum(case  when income_qty<>0 then size20 else -size20 end) as size20,sum(case  when income_qty<>0 then size21 else -size21 end) as size21,sum(case  when
income_qty<>0 then size22 else -size22 end) as size22,sum(case  when income_qty<>0 then size23 else -size23 end) as size23,sum(case  when income_qty<>0 then size24 else -size24
end) as size24,sum(case  when income_qty<>0 then size25 else -size25 end) as size25 from DRP_Inventory_Journal  Journal  Left Join Inventory On
Inventory.Inventory_ID=Journal.Inventory_ID   Left Join bas_Model On bas_Model.Model_id=Inventory.Model  Left Join vDRP_bas_Stock on vDRP_bas_stock.stock_id=Journal.stock_id
WHERE Inventory.Type=1 And (vDRP_bas_stock.stock_id='ZPA') AND     ContactCode like 'cnt036%' and Journal.date<='2019-04-01' and Journal.date>='2019-2-01' group by
Journal.Inventory_ID  ) as B ON A.Inventory_ID=B.Inventory_ID   left join Inventory on Inventory.Inventory_ID=a.Inventory_ID or Inventory.Inventory_ID=b.Inventory_ID option
(hash group)


vDRP_bas_Stock.contactcode like 'cnt036146%'