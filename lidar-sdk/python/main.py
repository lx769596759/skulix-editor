from numpy import *
import functools
from scipy import optimize
from matplotlib import pyplot as p
import sys

x = list(map(lambda x:float(x), sys.argv[1].split(',')))
y = list(map(lambda x:float(x), sys.argv[2].split(',')))
xc_2  = float(sys.argv[3])
yc_2 = float(sys.argv[4])
R_2 = float(sys.argv[5])

print(x)
print(y)
print(xc_2)
print(yc_2)
print(R_2)

# 输出图
p.close('all')
p.figure(facecolor='white')  # figsize=(7, 5.4), dpi=72,
p.axis('equal')

theta_fit = linspace(-pi, pi, 180)
x_fit2 = xc_2 + R_2 * cos(theta_fit)
y_fit2 = yc_2 + R_2 * sin(theta_fit)
p.plot(x_fit2, y_fit2, 'k--', label="Fitting Circle", lw=2)

p.plot([xc_2], [yc_2], 'gD', mec='r', mew=1)

# draw
p.xlabel('x')
p.ylabel('y')

# 数据
p.plot(x, y, 'ro', label='data', ms=8, mec='b', mew=1)
p.legend(loc='best', labelspacing=0.1)

# 标题
# p.grid()
p.title('Least Squares Circle')

p.savefig('java.png')

p.show()

p.close('all')
method_2  = "Fitting Circle"

basename = 'python'

# 质心坐标
x_m = mean(x)
y_m = mean(y)

print(x_m,y_m)

#修饰器：用于输出反馈
def countcalls(fn):
    "decorator function count function calls "

    @functools.wraps(fn)
    def wrapped(*args):
        wrapped.ncalls +=1
        return fn(*args)

    wrapped.ncalls = 0
    return wrapped

def calc_R(xc, yc):

    return sqrt((x - xc) ** 2 + (y - yc) ** 2)

@countcalls
def f_2(c):
    Ri = calc_R(*c)
    return Ri - Ri.mean()

#圆心估计
center_estimate = x_m, y_m
center_2, _ = optimize.leastsq(f_2, center_estimate)

xc_2, yc_2 = center_2
Ri_2       = calc_R(xc_2, yc_2)
#拟合圆的半径
R_2        = Ri_2.mean()
residu_2   = sum((Ri_2 - R_2)**2)
residu2_2  = sum((Ri_2**2-R_2**2)**2)
ncalls_2   = f_2.ncalls

#输出列表
fmt = '%-22s %10.15f %10.15f %10.15f %10d %10.6f %10.6f %10.2f'
print (('\n%-22s' +' %10s'*7) % tuple('Method Xc Yc Rc nb_calls std(Ri) residu residu2'.split()))
print('-'*(22 +7*(10+1)))
print(fmt % (method_2 , xc_2 , yc_2 , R_2 , ncalls_2 , Ri_2.std() , residu_2 , residu2_2 ))

#输出图
p.close('all')

def plot_all(residu2=False):

    p.figure(facecolor='white')  # figsize=(7, 5.4), dpi=72,
    p.axis('equal')

    theta_fit = linspace(-pi, pi, 180)

    x_fit2 = xc_2 + R_2 * cos(theta_fit)
    y_fit2 = yc_2 + R_2 * sin(theta_fit)
    p.plot(x_fit2, y_fit2, 'k--', label=method_2, lw=2)

    p.plot([xc_2], [yc_2], 'gD', mec='r', mew=1)

    # draw
    p.xlabel('x')
    p.ylabel('y')


    # 数据
    p.plot(x, y, 'ro', label='data', ms=8, mec='b', mew=1)
    p.legend(loc='best', labelspacing=0.1)

    #标题
    # p.grid()
    p.title('Least Squares Circle')

    p.savefig('python.png')

# plot_all(residu2=False)
plot_all(residu2=True )

p.show()
