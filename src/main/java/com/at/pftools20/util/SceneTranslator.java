package com.at.pftools20.util;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Labeled;

public class SceneTranslator {
	public static void translateScene(final Node rootNode)
	{
		System.out.println("Translating..." + rootNode.getClass().toString());
		
		if(rootNode instanceof Labeled)
		{
			final Labeled labeledNode = (Labeled) rootNode;
			labeledNode.setText(TranslatableText.formatString(labeledNode.getText()));
		}
		else if(rootNode instanceof Parent)
		{
			final Parent parentNode = (Parent) rootNode;
			parentNode.getChildrenUnmodifiable().forEach(node -> {
				
				translateScene(node);
				
			});
		}
	}
}
