package br.usp.each.saeg.code.forest.metaphor.assembler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.usp.each.saeg.code.forest.domain.TreeData;
import br.usp.each.saeg.code.forest.metaphor.Forest;
import br.usp.each.saeg.code.forest.metaphor.Size;
import br.usp.each.saeg.code.forest.metaphor.Trunk;
import br.usp.each.saeg.code.forest.ui.core.LogListener;
import br.usp.each.saeg.code.forest.util.CollectionUtils;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class SquareForest implements Forest {

	private final static Logger logger = LoggerFactory.getLogger(LogListener.class.getName());
	
    static final float RADIUS = Size.BIG.getRadius();
    private final ForestRestrictions restrictions = new ForestRestrictions();
    private List<SquareAssembler> matrix = new ArrayList<SquareAssembler>();
    private List<TransformGroup> tgs = new ArrayList<TransformGroup>();
    private List<Trunk> trunks = new ArrayList<Trunk>();

    public SquareForest(List<TreeData> arg) {
        List<TreeData> coveredTrees = TreeData.covered(arg);

        Collections.sort(coveredTrees);
        float xSize = 0;
        for (TreeData data : coveredTrees) {
            if (data.getScore() < 0 || data.getTotalLoCs() == 0) {
                continue;
            }
            Trunk tr = new Trunk(data, restrictions);
            trunks.add(tr);
            xSize += tr.getLinearSize();
        }
        xSize = xSize + ((coveredTrees.size() + 1) * RADIUS);
        float idealX = (float) (xSize / Math.sqrt(coveredTrees.size()));

        for (int i = 0; i < trunks.size(); i++) {
            SquareAssembler current = matrix.isEmpty() ? null : matrix.get(matrix.size()-1);
            if (null == current) {
                current = new SquareAssembler(idealX);
                matrix.add(current);
            }
            if (!current.add(trunks.get(i))) {
                matrix.add(new SquareAssembler(idealX));
                i--;
            }
        }
        debugMatrix();
        positionRows();
    }

    private void positionRows() {
        for (int i = 0; i < matrix.size(); i++) {
            SquareAssembler row = matrix.get(i);
            List<Trunk> trunks = row.getTrunk();
            float maxSize = getMaxX();
            float separator = row.getSeparatorSpace(maxSize);

            double rowZ = (-getZ() + (row.getZ() * i) + 1); //ok

            for (int j = 0; j < trunks.size(); j++) {
                Transform3D tr = new Transform3D();
                double trunkX = -getX() + (separator * (j + 1)) + row.getTrunk().get(j).maxLeftBranchSize() + (2 * 1) + row.sizeUntilTree(j);

                Vector3d position = new Vector3d(trunkX, 0, rowZ);
                tr.setTranslation(position);
                TransformGroup tg = new TransformGroup(tr);
                tg.addChild(trunks.get(j).getTransformGroup());
                tgs.add(tg);
            }
        }
    }

    private float getMaxX() {
        List<Float> xs = new ArrayList<Float>();
        for (SquareAssembler row : matrix) {
            xs.add(row.getX());
        }
        return CollectionUtils.max(xs);
    }

    void debugMatrix() {
        StringBuilder debug = new StringBuilder();
        for (int i = 0; i < matrix.size(); i++) {
            SquareAssembler list = matrix.get(i);
            List<Float> scores = new ArrayList<Float>();
            for (Trunk tr : list.getTrunk()) {
                scores.add(tr.getData().getScore());
            }
            debug.append(i).append(" - ").append(scores).append("\n");
        }
        logger.info(debug.toString());
    }

    @Override
    public void applyForestRestrictions() {
        for (Trunk trunk : getTrunks()) {
            trunk.refresh();
        }
    }

    @Override
    public List<TransformGroup> getTrees() {
        return tgs;
    }

    @Override
    public float getZ() {
        float z = 0;
        for (SquareAssembler row : matrix) {
            z += row.getZ();
        }

        return z/2;
    }

    @Override
    public float getX() {
        return getMaxX()/2;
    }

    @Override
    public float getY() {
        List<Float> ys = new ArrayList<Float>();
        for (SquareAssembler row : matrix) {
            for (Trunk trunk : row.getTrunk()) {
                ys.add(trunk.getHeight());
            }
        }
        return CollectionUtils.max(ys);
    }

    @Override
    public List<Trunk> getTrunks() {
        return trunks;
    }

    @Override
    public ForestRestrictions getRestrictions() {
        return restrictions;
    }

    @Override
    public void resetForestRestrictions() {
        restrictions.reset();
        applyForestRestrictions();
    }
}
