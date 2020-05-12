package com.jch.core.types.known.tx.result;

import java.util.Iterator;

import com.jch.core.coretypes.STArray;
import com.jch.core.coretypes.STObject;
import com.jch.core.coretypes.uint.UInt32;
import com.jch.core.coretypes.uint.UInt8;
import com.jch.core.serialized.enums.EngineResult;
import com.jch.core.types.known.sle.LedgerEntry;

public class TransactionMeta extends STObject {
	public static boolean isTransactionMeta(STObject source) {
		return source.has(UInt8.TransactionResult) && source.has(STArray.AffectedNodes);
	}

	public EngineResult engineResult() {
		return engineResult(this);
	}

	public Iterable<AffectedNode> affectedNodes() {
		STArray nodes = get(STArray.AffectedNodes);
		final Iterator<STObject> iterator = nodes.iterator();
		return new Iterable<AffectedNode>() {
			public Iterator<AffectedNode> iterator() {
				return iterateAffectedNodes(iterator);
			}
		};
	}

	public void walkPrevious(LedgerEntry.OnLedgerEntry cb) {
		for (AffectedNode affectedNode : affectedNodes()) {
			if (affectedNode.wasPreviousNode()) {
				cb.onObject(affectedNode.nodeAsPrevious());
			}
		}
	}

	public void walkFinal(LedgerEntry.OnLedgerEntry cb) {
		for (AffectedNode affectedNode : affectedNodes()) {
			if (affectedNode.isFinalNode()) {
				cb.onObject(affectedNode.nodeAsFinal());
			}
		}
	}

	public static Iterator<AffectedNode> iterateAffectedNodes(final Iterator<STObject> iterator) {
		return new Iterator<AffectedNode>() {
			public boolean hasNext() {
				return iterator.hasNext();
			}

			public AffectedNode next() {
				return (AffectedNode) iterator.next();
			}

			public void remove() {
				iterator.remove();
			}
		};
	}

	public UInt32 transactionIndex() {
		return get(UInt32.TransactionIndex);
	}
}
