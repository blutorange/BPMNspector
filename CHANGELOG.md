## 1.1.1

* Fix: Rule Ext.069 and Ext.070. The BPMN spec mandates that for multi-instance SubProcess, the loopDataInputRef and
  loopDataOutputRef attributes must point to a DataObjectReference, not a DataInput or DataOutput. These rules did
  not account for this fact properly, leading to false positives. 

## 1.1.0

* First release of fork