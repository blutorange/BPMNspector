## 1.1.2

* Fix: Remove check for rules Ext.069 and Ext.070. Upon closer investigation, these cannot be checked programmatically, and
  certainly not with Schematron. These rules require that the data structure of `inputDataItem` is the scalar type of
  the collection type of `loopDataInputRef` (same for `outputDataItem` and `loopDataOutputRef`). The rules made the incorrect
  assumption that simply setting ItemDefinition.isCollection to true is sufficient to turn a data type into a collection
  type -- this is not the case. For example, `<Item Definition structureRef="xsd:string" + isCollection="true"/>` is
  invalid according to the BPMN 2.0 spec, as `xsd:string` is not a collection type. Correct might be
  `<Item Definition structureRef="tns:collectionOfStrings" + isCollection="true"/>`. Resolving and comparing two
  different data types is beyond the scope of this project.

## 1.1.1

* Fix: Rule Ext.069 and Ext.070. The BPMN spec mandates that for multi-instance SubProcess, the loopDataInputRef and
  loopDataOutputRef attributes must point to a DataObject or DataObjectReference, not a DataInput or DataOutput. These
  rules did not account for this fact properly, leading to false positives. 

## 1.1.0

* First release of fork